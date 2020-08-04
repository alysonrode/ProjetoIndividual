Usuario = new Object();

isAdmin = function(){

    $.ajax({
        url: "/ERP/rest/SessionUtils/isAdmin",
        type: "POST",
        success : function (admin) {
            console.log(admin);
            Usuario.isAdmin = admin;
            getName()
            admin == true ? loadAdmin() : loadNormalUser();
        }
    })
}
getName = function(){
    $.ajax({
        url: "/ERP/rest/SessionUtils/getName",
        type: "POST",
        success : function (name) {
            document.getElementById("botaoUsuario").innerHTML = "<img src=\"../imagens/userIcon.png\" id=\"imgHeader\"> " + name.replaceAll('"', "")
        }
    })
}

$(document).ready(function(){
    isAdmin()
    loadAdmin = function () {
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
    }

    loadNormalUser = function (){
        $("#Content").load("/ERP/vendedor/index.html")
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
