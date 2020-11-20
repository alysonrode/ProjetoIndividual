var modalExibeProdutosVenda
showProducts = function (id) {
    var busca = "";
    modalExibeProdutosVenda = {
        height: 450,
        width: 400,
        title: "Selecione o produto",
        modal: true,
    }
    getProductsSells(busca, id)
    $("#producsShowdown").dialog(modalExibeProdutosVenda);
}

prepareSearch = function () {
    var busca = document.getElementById("buscaProdutoVendas").value;
    getProductsSells(busca)
}

getProductsSells = function (busca, id) {
    $.ajax({
        url: "/ERP/rest/produtos/buscar",
        type: "GET",
        data: "busca=" + busca,
        success: function (listaDeProdutos) {
            $("#producsShowdown").html(prepareList(listaDeProdutos, id))
        },
        error: function (msg) {
            exibirAviso("msg")
        }
    })
}

prepareList = function (listaDeProdutos, id) {

    var html = "Buscar: <input type=\"text\" id=\"buscaProdutoVendas\" onkeypress=\"getProducts()\" class=\"outline-dark\"/>";

    for (var i = 0; i < listaDeProdutos.length; i++) {
        html += "<br><br> <a class=\"btn btn-outline-dark\" onclick=\"selecionaProduto(\'" +

            id + "\', \'" + listaDeProdutos[i].nome + "\',\'" + listaDeProdutos[i].marca + "\',\'"
            + listaDeProdutos[i].valor + "\',\'" + listaDeProdutos[i].id + "\',\'" + false + "\')\">" +

            "" + listaDeProdutos[i].id + " - " + listaDeProdutos[i].nome + " - " + listaDeProdutos[i].marca + "</a>"
    }

    return html;
}

selecionaProduto = function (id, nome, marca, valor, idProduto, update) {

    var produtoString = nome + "-" + marca;
    document.getElementById(id).value = produtoString;
    document.getElementById("idProduto" + id.replace("inputProduto", "")).value = idProduto;
    idValor = "inputValorUnit" + id.replace("inputProduto", "");
    document.getElementById(idValor).value = moneyMask(valor);

    calculaValor("inputQuantidade" + id.replace("inputProduto", ""))
    if(update == "false"){$("#producsShowdown").dialog('close')}

}

calculaValor = function (idQuantidade) {

    idValor = "inputValorTotal" + idQuantidade.toString().replace("inputQuantidade", "");
    idValorUnit = "inputValorUnit" + idQuantidade.toString().replace("inputQuantidade", "");

    valor = document.getElementById(idValorUnit.toString()).value;
    quant = document.getElementById(idQuantidade.toString()).value

    document.getElementById(idValor).value = moneyMask(quant * removeMoneyMask(valor));
}

moneyMask = function (valor) {
    valor = valor.toString()
    if (!valor.includes(".")) {
        valorNovo = "R$" + valor + ".00";
    } else {
        var stringsplited = valor.split(".");
        if(stringsplited[1].length == 1){
            valorNovo = "R$" + valor + "0";
        }else{
            valorNovo = "R$" + valor;
        }
    }
    return valorNovo;
}

removeMoneyMask = function (valor) {

    valor = valor.replace("R$", "");

    return valor;

}

createLine = function (update) {
    var form = null;
    if(update){
        form = document.getElementById("produtosUpdate")
    }else {
        form = document.getElementById("formVendas")
    }
    form.appendChild(productLine(update))
}
productLine = function (update) {

    var count = getId()
    var divSub = document.createElement('div')
    divSub.setAttribute('id', 'linha' + count)
    divSub.setAttribute('class', "form-row inputsCadastro2")
    var divProduto = document.createElement('div')
    divProduto.setAttribute('id', 'produtoVenda' + count)
    var inputProduto = document.createElement('input')
    inputProduto.setAttribute('type', 'text')
    inputProduto.setAttribute('readonly', '')
    inputProduto.setAttribute('placeholder', 'Mostrar produtos')
    inputProduto.setAttribute('class', 'form-control inputsProduto')
    inputProduto.setAttribute('id', 'inputProduto' + count)
    inputProduto.setAttribute('onclick', 'showProducts(\"inputProduto' + count + '\")')
    var inputIdProduto = document.createElement('input')
    inputIdProduto.setAttribute('type', 'text')
    inputIdProduto.setAttribute('id', 'idProduto' + count)
    inputIdProduto.setAttribute('hidden', '')
    inputIdProduto.setAttribute('value', '')
    var divQuantidade = document.createElement('div')
    divQuantidade.setAttribute('id', 'quantidadeVenda' + count)
    var inputQuantidade = document.createElement('input')
    inputQuantidade.setAttribute('type', 'number')
    inputQuantidade.setAttribute('class', 'form-control')
    inputQuantidade.setAttribute('onkeypress', 'calculaValor(\'inputQuantidade' + count + '\')')
    inputQuantidade.setAttribute('id', 'inputQuantidade' + count)
    inputQuantidade.setAttribute('placeholder', 'Quantidade')
    var divValorUnit = document.createElement('div')
    divValorUnit.setAttribute('id', 'valorUnitVenda' + count)
    var inputValorUnit = document.createElement('input')
    inputValorUnit.setAttribute('type', 'text')
    inputValorUnit.setAttribute('class', 'form-control')
    inputValorUnit.setAttribute('readonly', '')
    inputValorUnit.setAttribute('id', 'inputValorUnit' + count)
    inputValorUnit.setAttribute('value', 'R$0.00')
    var divValorTotal = document.createElement('div')
    divValorTotal.setAttribute('id', 'divValorTotal' + count)
    var inputValorTotal = document.createElement('input')
    inputValorTotal.setAttribute('type', 'text')
    inputValorTotal.setAttribute('class', 'form-control')
    inputValorTotal.setAttribute('readonly', '')
    inputValorTotal.setAttribute('id', 'inputValorTotal' + count)
    inputValorTotal.setAttribute('value', 'R$0.00')
    var divIcones = document.createElement('div')
    divIcones.setAttribute('class', 'iconsClass2')
    var primeiroIcone = document.createElement('img')
    primeiroIcone.setAttribute('class', 'icons icons2')
    primeiroIcone.setAttribute('src', '/ERP/imagens/plusIcon.jpg')
    if(!update){
        primeiroIcone.setAttribute('onclick', 'createLine(' + false +')')
    }else{
        primeiroIcone.setAttribute('onclick', 'createLine(' + true + ')')
    }
    var terceiroIcone = document.createElement('img')
    terceiroIcone.setAttribute('class', 'icons icons2')
    terceiroIcone.setAttribute('src', '/ERP/imagens/trashIcon.png')
    terceiroIcone.setAttribute('onclick', 'deleteLine(\'linha' + count + '\')')

    divProduto.appendChild(inputProduto)
    divQuantidade.appendChild(inputQuantidade)
    divValorTotal.appendChild(inputValorTotal)
    divValorUnit.appendChild(inputValorUnit)

    divIcones.appendChild(primeiroIcone)
    divIcones.appendChild(terceiroIcone)

    divSub.appendChild(divProduto)
    divSub.appendChild(divQuantidade)
    divSub.appendChild(divValorUnit)
    divSub.appendChild(divValorTotal)
    divSub.appendChild(inputIdProduto)


    var html = document.createElement("div")
    html.setAttribute('class', 'form-group row')
    html.setAttribute('id', 'linha' + count)
    html.appendChild(divSub)
    html.appendChild(divIcones)

    return html
}

deleteLine = function (id) {
    var element = document.getElementById(id)
    element.remove()
}
getId = function () {
    var count = 1;
    for (var i = 0; i < count; i++) {
        var element = document.getElementById('linha' + count)
        if (element == null) {
            return count;
        } else {
            count++
        }
    }
}
pegaDataAtual = function () {

    var today = new Date();

    var day = today.getDate().toString().length < 2 ? "0" + today.getDate() : today.getDate()

    var month = (today.getMonth() + 1).toString().length < 2 ? "0" + (today.getMonth() + 1) : (today.getMonth() + 1)

    dataString = day + "/" + month + "/" + today.getFullYear();

    if(document.getElementById('staticDate') != null){
        document.getElementById('staticDate').value = dataString
    }
    return dataString;

}

prepareSell = function () {
    lastId = getId() - 1;

    linha = new Object()
    sell = []
    for (i = 0; i < lastId; i++) {
        var produto = 'produto' + (i + 1)
        linha = {
            quantidade: document.getElementById("inputQuantidade" + (i + 1)).value,
            idProduto: document.getElementById("idProduto" + (i + 1)).value
        }
        sell.push(linha);
    }
    if (!validaVendas()) {
        exibirAviso("Campo inválido.")
    } else {
        try {
            preparaQuantidades(lastId, sell)
        } catch (e) {
        }
    }
}
cadastraVenda = function (venda) {
    $.ajax({
        url: "/ERP/rest/vendas/cadastrar",
        type: "post",
        data: JSON.stringify(venda),
        success: function (msg) {
            exibirAviso(msg)
        },
        error: function (msg) {
            exibirAviso(msg)
        }
    })
}
validaVendas = function () {
    lastId = getId() - 1
    for (i = 0; i < lastId; i++) {
        produto = document.getElementById("inputProduto" + (i + 1)).value
        if (produto == "" || produto == undefined) {
            document.getElementById("inputProduto" + (i + 1)).focus();
            return false;
        }
        quantidade = document.getElementById("inputQuantidade" + (i + 1)).value;
        if (quantidade == "" || quantidade == undefined) {
            document.getElementById("inputQuantidade" + (i + 1)).focus();
            return false;
        }
    }
    return true;
}
preparaQuantidades = function (lastId, sell) {
    listaIds = []
    quantidadesEmVenda = []
    for (i = 0; i < lastId; i++) {
        quantidade = document.getElementById("inputQuantidade" + (i + 1)).value;
        idProduto = document.getElementById("idProduto" + (i + 1)).value;
        listaIds.push(idProduto);
        quantidadesEmVenda.push(quantidade);
    }
    $.ajax({
        url: "/ERP/rest/produtos/pegaQuantidade",
        type: "POST",
        data: JSON.stringify(listaIds),
        success: function (listaQuantidadesRest) {
            try {
                if (validaQuantidades(listaQuantidadesRest, quantidadesEmVenda)) {
                    cadastraVenda(sell);
                } else {
                    exibirAviso("Quantidade insuficiente em estoque!")
                }
            } catch (e) {
                throw new Error();
            }
        },
        error: function () {
            exibirAviso("Erro desconhecido, contate o administrador.")
        }
    })
    return true;
}
validaQuantidades = function (listaQuantidades, quantidadesEmVenda) {
    for (i = 0; i < listaQuantidades.length; i++) {
        if (listaQuantidades[i] < quantidadesEmVenda[i]) {
            document.getElementById("idProduto" + (i + 1)).focus();
            return false;
        }
    }
    return true;
}
buscarVendas = function (pagina, minValue, maxValue, firstTime) {
    busca = document.getElementById("buscaVendas").value;
    $.ajax({
        url: "/ERP/rest/vendas/buscar",
        type: "GET",
        data: "busca=" + busca +"&minValue=" + minValue + "&maxValue="+maxValue,
        success: function (listaVendas) {
            if(firstTime){
                calculePagination(listaVendas, pagina)
            }else{
                $("#vendas").html(montaVendas(listaVendas, pagina))
            }
        }
    })

}

calculePagination = function(listaVendas, pagina){

    var pagMax = 4;
    var quantPag = Math.ceil(listaVendas.length / pagMax)

    var lastValue = 0;
    var initialValue = 0;

    var pagination = "";
    for(var x = 0; x < quantPag; x++){

        lastValue = (x +1) * pagMax;
        initialValue = lastValue - pagMax;
        pagination += "<li class=\"page-item\" onclick='buscarVendas("+ (x + 1) + "," + initialValue + "," + pagMax + ","+ false +")'><a class=\"page-link\" href=\"#\">" + (x + 1) + "</a></li>"
    }
    $("#pagination").html(pagination);
    buscarVendas(1, 0, pagMax, false)

}

montaVendas = function (listaVendas, pagina) {

    var html = "<tr>\n" +
        "<th>ID da Venda</th>\n" +
        "<th>Data da venda</th>\n" +
        "<th>Vendedor</th>\n" +
        "<th>Total vendido</th>\n" +
        "<th>Ações</th>\n" +
        "</tr>";

        for (i = 0; i < listaVendas.length; i++) {
            if(listaVendas[i] != undefined){
                html += "<tr>" +
                    "<td>" + listaVendas[i].idVenda + "</td>" +
                    "<td>" + listaVendas[i].dataVenda + "</td>" +
                    "<td>" + listaVendas[i].usuario.firstName + "</td>" +
                    "<td>" + moneyMask(listaVendas[i].valorTotal) + "</td>" +
                    "<td>" +
                    "<img class=\"iconsHistoricVendas\" onclick='alteraVenda(" + listaVendas[i].idVenda + ")' src=\"/ERP/imagens/editIcon.jpeg\">" +
                    "<img class=\"iconsHistoricVendas\" onclick='excluiVenda(" + listaVendas[i].idVenda + ")' src=\"/ERP/imagens/trashIcon.png\">" +
                    "</td>" +
                    "</tr>"
            }else{
                break;
            }
        }
    return html;
}
alteraVenda = function (idVenda) {
    $.ajax({
        url: "/ERP/rest/vendas/selecionar",
        type: "POST",
        data: JSON.stringify(idVenda),
        success : function (Venda) {
            montaAlteracao(Venda)
        },
        error : function (msg) {

        }
    })
}

montaAlteracao = function (Venda) {

    document.getElementById("idVenda").value = Venda.idVenda;
    document.getElementById("valorTotal").value = moneyMask(Venda.valorTotal);
    document.getElementById("dataVenda").value = Venda.dataVenda;
    document.getElementById("vendedor").value = Venda.usuario.firstName;

    for(i = 0; i < Venda.listProduts.length; i++){
        createLine(true);
        selecionaProduto("inputProduto" + (i+1) ,Venda.listProduts[i].nome, Venda.listProduts[i].marca, Venda.listProduts[i].valor,
            Venda.listProduts[i].id, true)
        document.getElementById("inputQuantidade" + (i+1)).value = Venda.listProduts[i].quantidade
        calculaValor("inputQuantidade" + (i +1))
    }

    var modalEditaVenda = {
        height: 550,
        width: 1100,
        title: "Alteração de Vendas",
        modal: true,
        buttons: {
            "Salvar": function () {
                if(validaVendas()){
                    cadastraAlteracao();
                }
            },
            "Cancelar": function () {
                //document.getElementById('frmProduto').reset();
                $(this).dialog("close")
            }
        }
    }
    $("#modalAlteracaoVenda").dialog(modalEditaVenda)
}
cadastraAlteracao = function () {

    var lastId = (getId() -1);

    var idVenda = document.getElementById("idVenda").value
    var idProduto = "idProduto"
    var idQuantidade = "inputQuantidade"
    var venda = [];
    venda.push(idVenda);
    for (i=0; i < lastId; i++){
        var produto = {
            quantidade: document.getElementById(idQuantidade + (i+1)).value,
            idProduto: document.getElementById(idProduto+(i + 1)).value

        }
        venda.push(produto);
    }
    $.ajax({
        url: "/ERP/rest/vendas/update",
        type: "POST",
        data: JSON.stringify(venda),
        success : function (msg) {
            exibirAviso(msg)
            $("#modalAlteracaoVenda").dialog('close');
            buscarVendas(1, -1, -1, true);
        },
        error : function (msg) {
          exibirAviso(msg)
        }
    })

}

excluiVenda = function (id) {

    var modalExclusao = {
        height: 100,
        width: 450,
        title: "Confirmação",
        modal: true,
        buttons: {
            "Sim": function () {
                $.ajax({
                    url:"/ERP/rest/vendas/deletar",
                    type: "GET",
                    data: "id="+id,
                    success : function (msg) {
                        exibirAviso(msg)
                        $("#modalExclusaoVenda").dialog("close");
                        buscarVendas(1,-1,-1, true)
                    },
                    error : function (msg) {
                        exibirAviso(msg.responseText)
                    }
                })
            },
            "Não":function () {
                $(this).dialog("close");
            }
        }
    }
    $("#modalExclusaoVenda").dialog(modalExclusao);

}