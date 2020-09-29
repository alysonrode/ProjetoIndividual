getProducts = function () {
    var busca = document.getElementById("buscaProduto").value
    $.ajax({
        url: "/ERP/rest/produtos/buscar",
        type: "GET",
        data: "busca=" +busca,
        success : function (listaProdutos) {
            $("#tabelaProduto").html(montaProdutos(listaProdutos))
        },
        error : function () {

        }
    })
}
cadastraProduto = function () {
    $.ajax({
        type: "POST",
        url: "/ERP/rest/produtos/buscaid",
        success : function(id){
            document.frmProduto.idProduto.value = id;
            var modalCadastraProduto = {
                height: 450,
                width: 400,
                title: "Cadastro de Produtos",
                modal: true,
                buttons: {
                    "Salvar": function () {
                        insereMarca()
                        $(this).dialog("close")
                    },
                    "Cancelar": function () {
                        document.getElementById('frmProduto').reset();
                        $(this).dialog("close")
                    }
                }
            }
            $("#modalProduto").dialog(modalCadastraProduto);


        }
    })
}
insereMarca = function () {
    Produto = new Object()

    Produto.id = document.frmProduto.idProduto.value;
    Produto.quantidade = document.frmProduto.quantidade.value;
    Produto.nome = document.frmProduto.nomeProduto.value;
    Produto.marca = document.frmProduto.marcaProduto.value;
    Produto.valor = document.frmProduto.valor.value;

    $.ajax({
        url: "/ERP/rest/produtos/cadastrar",
        type: "POST",
        data: JSON.stringify(Produto),
        success: function (msg) {
            exibirAviso(msg)
            document.getElementById('frmProduto').reset();
            getProducts();
        },
        error : function (msg) {
            exibirAviso(msg)
        }
    })
}
montaProdutos = function (listaProdutos) {

    var html = "<tr>\n" +
        "<th>ID</th>\n" +
        "<th>Produto</th>\n" +
        "<th>Marca</th>\n" +
        "<th>Quatidade</th>\n" +
        "<th>Valor Unit</th>\n" +
        "<th>Ações</th>\n" +
        "</tr>"
    for(var i = 0; i < listaProdutos.length; i++){
        html +=
            "<tr id=\"itemActions"+ i +"\">\n" +
            "<td id=\'Produto" + i + "\'>" + listaProdutos[i].id + "</td>\n" +
            "<td>" + listaProdutos[i].nome + "</td>\n" +
            "<td>" + listaProdutos[i].marca + "</td>\n" +
            "<td>" + listaProdutos[i].quantidade + "</td>\n" +
            "<td>" + "R$" +listaProdutos[i].valor + "</td>\n" +
            "<td>\n" +
            "<img class=\"iconsHistoric\" onclick='getProductToUpdate(" + listaProdutos[i].id + ")' src=\"/ERP/imagens/editIcon.jpeg\">\n" +
            "<img class=\"iconsHistoric\" onclick='deleteProcuct(" + listaProdutos[i].id + ")' src=\"/ERP/imagens/trashIcon.png\">\n" +
            "</td>\n" +
            "</tr>"
    }
    return html;
}
getProductToUpdate = function (id) {
    $.ajax({
        url: "/ERP/rest/produtos/getProductById",
        type: "GET",
        data: "id=" + id,
        success : function (Product) {
            openModalToUpdate(Product)
        },
        error : function () {

        }
    })
}
openModalToUpdate = function (Product) {
    document.frmProduto.quantidade.value = Product.quantidade
    document.frmProduto.nomeProduto.value = Product.nome
    document.frmProduto.marcaProduto.value = Product.marca
    document.frmProduto.idProduto.value = Product.id
    document.frmProduto.valor.value = Product.valor

    var modalAtualizaProduto = {
        height: 450,
        width: 400,
        title: "Alterar produto",
        modal: true,
        buttons: {
            "Salvar": function () {
                Product.id = document.frmProduto.idProduto.value;
                Product.quantidade = document.frmProduto.quantidade.value;
                Product.nome = document.frmProduto.nomeProduto.value;
                Product.marca = document.frmProduto.marcaProduto.value;
                Product.valor = document.frmProduto.valor.value;
                updateProduct(Product);
                document.getElementById('frmProduto').reset();
                $(this).dialog("close")
                },
            "Cancelar": function () {
                document.getElementById('frmProduto').reset();
                $(this).dialog("close")
            }
        }
    }
    $("#modalProduto").dialog(modalAtualizaProduto);
}
updateProduct = function (Product) {
    $.ajax({
        url:"/ERP/rest/produtos/updateProduct",
        type: "POST",
        data: JSON.stringify(Product),
        success: function (msg) {
            exibirAviso(msg)
            getProducts()
        },
        error : function (msg) {
            exibirAviso(msg)
        }
    })
}
deleteProcuct = function (id) {
    var modaldeleteproduto = {
        height: 200,
        width: 400,
        title: "Excluir",
        modal: true,
        buttons: {
            "Sim": function () {
                $.ajax({
                    url: "/ERP/rest/produtos/deleteProduct",
                    type: "GET",
                    data: "id=" + id,
                    success: function (msg) {
                        exibirAviso(msg)
                        getProducts()
                    },
                    error: function (msg) {
                        exibirAviso(msg)
                    }
                })
                $(this).dialog("close")
            },
            "Não": function() {$(this).dialog("close")}
        }
    }
    $("#modalExclusaoProduto").dialog(modaldeleteproduto);


}
