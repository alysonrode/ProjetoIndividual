$(document).ready(function(){

$("#Content").load("/ERP/home/main/main.html");
$("#sidebar").load("/ERP/home/sidebar/sidebar.html");

    carregaPrincipal = function(){
        $("#Content").load("/ERP/home/main/main.html")
    }
    carregaVendas = function () {
        $("#Content").load("/ERP/home/vendas/vendas.html")
    }
    carregaEstoque = function () {
        $("#Content").load("/ERP/home/estoque/estoque.html")
    }
    carregaRelatorios = function () {
        $("#Content").load("/ERP/home/relatorios/relatorios.html")
    }
    carregaHistoricoVendas = function () {
        $("#vendasContent").load("/ERP/home/vendas/historicoVendas.html")
    }
    carregaUsuarios = function (){
        $("#Content").load("/ERP/home/usuarios/main.html")
    }

    exibirAviso = function(aviso){

        var modal={
            title: "Mensagem",
            height: 250,
            width: 400,
            modal: true,
            buttons: {
                "OK": function(){
                    $(this).dialog("close");
                }
            }
        };
        $("#modalAviso").html(aviso);
        $("#modalAviso").dialog(modal);
    };

})
