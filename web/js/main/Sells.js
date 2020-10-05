var modalExibeProdutosVenda
showProducts = function (id){
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

prepareSearch = function(){
    var busca = document.getElementById("buscaProdutoVendas").value;
    getProductsSells(busca)
}

getProductsSells = function(busca, id){
    $.ajax({
        url: "/ERP/rest/produtos/buscar",
        type:"GET",
        data: "busca="+busca,
        success: function (listaDeProdutos) {
            $("#producsShowdown").html(prepareList(listaDeProdutos, id))
        },
        error: function (msg) {
            exibirAviso("msg")
        }
    })
}

prepareList = function(listaDeProdutos, id){

    var html = "Buscar: <input type=\"text\" id=\"buscaProdutoVendas\" onkeypress=\"getProducts()\" class=\"outline-dark\"/>";

    for(var i =0; i < listaDeProdutos.length; i++){
        html += "<br><br> <a class=\"btn btn-outline-dark\" onclick=\"selecionaProduto(\'" +

            id + "\', \'" + listaDeProdutos[i].nome + "\',\'"+ listaDeProdutos[i].marca + "\',\'"
            + listaDeProdutos[i].valor + "\',\'" + listaDeProdutos[i].id +"\')\">" +

            "" + listaDeProdutos[i].id + " - " +listaDeProdutos[i].nome + " - " + listaDeProdutos[i].marca +"</a>"
    }

    return html;
}

selecionaProduto = function(id, nome, marca, valor, idProduto){

    var produtoString = nome + "-" + marca;
    document.getElementById(id).value = produtoString;
    document.getElementById("idProduto"+id.replace("inputProduto","")).value = idProduto;

    idValor = "inputValorUnit" + id.replace("inputProduto","");
    document.getElementById(idValor).value = moneyMask(valor);

    calculaValor("inputQuantidade" + id.replace("inputProduto",""))

    $("#producsShowdown").dialog('close');
}

calculaValor = function(idQuantidade){

    idValor = "inputValorTotal" + idQuantidade.replace("inputQuantidade","");
    idValorUnit = "inputValorUnit" + idQuantidade.replace("inputQuantidade","");

    valor = document.getElementById(idValorUnit).value;
    quant = document.getElementById(idQuantidade).value

    document.getElementById(idValor).value = moneyMask(quant * removeMoneyMask(valor));
}

moneyMask = function(valor){
    valor = valor.toString()
    if(!valor.includes(".")){
        valorNovo = "R$" + valor + ".00";
    }
    else{
        valorNovo = "R$" + valor;
    }
    return valorNovo;
}

removeMoneyMask = function(valor){

    valor = valor.replace("R$", "");

    return valor;

}

createLine = function () {
    var form = document.getElementById("formVendas")
    form.appendChild(productLine())
}
productLine = function () {

    var count = getId()
    var divSub = document.createElement('div')
    divSub.setAttribute('id','linha'+count)
    divSub.setAttribute('class',"form-row inputsCadastro2")
    var divProduto = document.createElement('div')
    divProduto.setAttribute('id', 'produtoVenda' + count)
    var inputProduto = document.createElement('input')
    inputProduto.setAttribute('type','text')
    inputProduto.setAttribute('readonly', '')
    inputProduto.setAttribute('placeholder','Mostrar produtos')
    inputProduto.setAttribute('class','form-control inputsProduto')
    inputProduto.setAttribute('id','inputProduto' + count)
    inputProduto.setAttribute('onclick', 'showProducts(\"inputProduto'+count +'\")')
    var inputIdProduto = document.createElement('input')
    inputIdProduto.setAttribute('type', 'text')
    inputIdProduto.setAttribute('id','idProduto' + count)
    inputIdProduto.setAttribute('hidden','')
    inputIdProduto.setAttribute('value','')
    var divQuantidade = document.createElement('div')
    divQuantidade.setAttribute('id','quantidadeVenda' + count)
    var inputQuantidade = document.createElement('input')
    inputQuantidade.setAttribute('type', 'number')
    inputQuantidade.setAttribute('class', 'form-control')
    inputQuantidade.setAttribute('onclick', 'calculaValor(inputQuantidade'+ count +')' )
    inputQuantidade.setAttribute('id', 'inputQuantidade' + count)
    inputQuantidade.setAttribute('placeholder', 'Quantidade')
    var divValorUnit = document.createElement('div')
    divValorUnit.setAttribute('id','valorUnitVenda'+count)
    var inputValorUnit = document.createElement('input')
    inputValorUnit.setAttribute('type','text')
    inputValorUnit.setAttribute('class','form-control')
    inputValorUnit.setAttribute('readonly', '')
    inputValorUnit.setAttribute('id','inputValorUnit' + count)
    inputValorUnit.setAttribute('value','R$0.00')
    var divValorTotal = document.createElement('div')
    divValorTotal.setAttribute('id','inputValorTotal' + count)
    var inputValorTotal = document.createElement('input')
    inputValorTotal.setAttribute('type','text')
    inputValorTotal.setAttribute('class','form-control')
    inputValorTotal.setAttribute('readonly', '')
    inputValorTotal.setAttribute('id','inputValorTotal' + count)
    inputValorTotal.setAttribute('value','R$0.00')
    var divIcones = document.createElement('div')
    divIcones.setAttribute('class','iconsClass2')
    var primeiroIcone = document.createElement('img')
    primeiroIcone.setAttribute('class','icons icons2')
    primeiroIcone.setAttribute('src','/ERP/imagens/plusIcon.jpg')
    primeiroIcone.setAttribute('onclick','createLine()')
    var segundoIcone = document.createElement('img')
    segundoIcone.setAttribute('class','icons icons2')
    segundoIcone.setAttribute('src','/ERP/imagens/editIcon.jpeg')
    var terceiroIcone = document.createElement('img')
    terceiroIcone.setAttribute('class','icons icons2')
    terceiroIcone.setAttribute('src','/ERP/imagens/trashIcon.png')
    terceiroIcone.setAttribute('onclick', 'deleteLine(\'linha'+ count +'\')')

    divProduto.appendChild(inputProduto)
    divQuantidade.appendChild(inputQuantidade)
    divValorTotal.appendChild(inputValorTotal)
    divValorUnit.appendChild(inputValorUnit)

    divIcones.appendChild(primeiroIcone)
    divIcones.appendChild(segundoIcone)
    divIcones.appendChild(terceiroIcone)

    divSub.appendChild(divProduto)
    divSub.appendChild(divQuantidade)
    divSub.appendChild(divValorUnit)
    divSub.appendChild(divValorTotal)
    divSub.appendChild(inputIdProduto)


    var html = document.createElement("div")
    html.setAttribute('class', 'form-group row')
    html.setAttribute('id', 'linha'+count)
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
    for (var i = 0; i < count; i++){
        var element = document.getElementById('linha'+count)
        if(element == null){
            return count;
        }
        else{
            count++
        }
    }
}
pegaDataAtual = function () {

    var today = new Date();

    var day = today.getDate().toString().length < 2 ? "0" + today.getDate() : today.getDate()

    var month = (today.getMonth() + 1).toString().length < 2 ? "0"+ (today.getMonth() + 1): (today.getMonth() + 1)
    
    dataString = day + "/" + month + "/" + today.getFullYear();

    document.getElementById('staticDate').value = dataString;

}

prepareSell = function () {
    lastId = getId() - 1;

    linha = new Object()
    sell = []
    for(i = 0; i < lastId; i++) {
        var produto = 'produto' + (i + 1)
        linha = {
            produto: document.getElementById("inputProduto" + (i + 1)).value,
            quantidade: document.getElementById("inputQuantidade" + (i + 1)).value,
            idProduto: document.getElementById("idProduto"+(i+1)).value
        }
        sell.push(linha);
    }
    if(!validaVendas()){
        exibirAviso("Campo inválido.")
    }
    if(!validaQuantidade(lastId)){
        exibirAviso("Quantidade inválida")
    }
    else{
        cadastraVenda(sell)
    }
}
cadastraVenda = function (venda) {
    $.ajax({
        url: "/ERP/rest/vendas/cadastrar",
        type:"post",
        data: JSON.stringify(venda),
        success : function (msg) {
            exibirAviso(msg)
        },
        error : function (msg) {
            exibirAviso(msg)
        }
    })
}
validaVendas = function () {
    lastId = getId() - 1
    for (i = 0; i < lastId; i++){
        produto = document.getElementById("inputProduto" + (i + 1)).value
        if(produto == "" || produto == undefined){
            document.getElementById("inputProduto" + (i + 1)).focus();
            return false;
        }
        quantidade = document.getElementById("inputQuantidade" + (i + 1)).value;
        if(quantidade == "" || quantidade == undefined){
            document.getElementById("inputQuantidade" + (i + 1)).focus();
            return false;
        }
    }
    return true;
}
validaQuantidade = function (lastId) {
    listaIds = []
    listaQuantidades = []
    quantidadesEmVenda = []
    for(i = 0; i < lastId; i++){
        quantidade = document.getElementById("inputQuantidade" + (i + 1)).value;
        idProduto = document.getElementById("idProduto"+(i+1)).value;
        listaIds.push(idProduto);
        quantidadesEmVenda.push(quantidade);
    }
    $.ajax({
        url: "/ERP/rest/produtos/pegaQuantidade",
        type: "POST",
        data: JSON.stringify(listaIds),
        success : function (listaQuantidadesRest) {
            listaQuantidades = listaQuantidadesRest;
        },
        error : function () {
            exibirAviso("Erro desconhecido, contate o administrador.")
        }
    })

    for (i = 0; i < listaQuantidades.length; i++){
        if(listaQuantidades[i] < quantidadesEmVenda[i]){
            document.getElementById("idProduto"+(i+1)).focus();
            return false;
        }
    }
    return true;
}