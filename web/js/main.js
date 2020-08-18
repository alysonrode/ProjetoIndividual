Usuario = new Object();

checkSession = function(){
    $.ajax({
        url: "/ERP/rest/SessionUtils/checkSession",
        type: "POST",
        success : function (SessionOk) {
            if (SessionOk == false){location.reload()}
        }
    })
}

isAdmin = function(){

    $.ajax({
        url: "/ERP/rest/SessionUtils/isAdmin",
        type: "POST",
        success : function (admin) {
            Usuario.isAdmin = admin;
            getName()
            admin == "true" ? loadAdmin() : loadNormalUser();
        }
    })
}
getName = function(){
    $.ajax({
        url: "/ERP/rest/SessionUtils/getName",
        type: "POST",
        success : function (name) {
            document.getElementById("botaoUsuario").innerHTML =
                "<img src=\"../imagens/userIcon.png\" id=\"imgHeader\"> " +
                name.replaceAll('"', "")
        }
    })
}
logout = function(){

    $.ajax({
        url: "/ERP/rest/SessionUtils/logout",
        type: "POST",
        success : function (msg) {
            location.reload();
        },
        error : function (msg) {
            location.reload();
            exibirAviso(msg)
        }
    })
}

$(document).ready(function(){
    isAdmin()
})
    loadAdmin = function () {
        $("#Content").load("/ERP/home/main/main.html");

        $("#sidebar").load("/ERP/home/sidebar/sidebar.html");

            carregaPrincipal = function(){
                checkSession()
                $("#Content").load("/ERP/home/main/main.html")
            }
            carregaVendas = function () {
                checkSession()
                $("#Content").load("/ERP/home/vendas/vendas.html")
            }
            carregaEstoque = function () {
                checkSession()
                $("#Content").load("/ERP/home/estoque/estoque.html")
            }
            carregaRelatorios = function () {
                checkSession()
                $("#Content").load("/ERP/home/relatorios/relatorios.html")
            }
            carregaHistoricoVendas = function () {
                checkSession()
                $("#vendasContent").load("/ERP/home/vendas/historicoVendas.html")
            }
            carregaUsuarios = function (){
                checkSession()
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


