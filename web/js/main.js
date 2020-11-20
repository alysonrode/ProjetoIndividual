$(document).ready(function () {

    Usuario = new Object();

    checkSession = function () {
        retorno = true;
        $.ajax({
            url: "/ERP/rest/SessionUtils/checkSession",
            type: "POST",
            success: function (SessionOk) {
                if (SessionOk == "true") {
                    retorno = true;
                } else {
                    retorno = false;
                }

            }
        })
        return retorno;
    }

    isAdmin = function () {

        $.ajax({
            url: "/ERP/rest/SessionUtils/isAdmin",
            type: "POST",
            success: function (admin) {
                Usuario.isAdmin = admin;
                getName()
                admin == "true" ? loadAdmin() : loadNormalUser();
            }
        })
    }
    getName = function () {
        $.ajax({
            url: "/ERP/rest/SessionUtils/getName",
            type: "POST",
            success: function (name) {
                document.getElementById("botaoUsuario").innerHTML =
                    "<img src=\"../imagens/userIcon.png\" id=\"imgHeader\"> " +
                    name.replaceAll('"', "")
            }
        })
    }
    logout = function () {

        $.ajax({
            url: "/ERP/rest/SessionUtils/logout",
            type: "POST",
            success: function (msg) {
                location.reload();
            },
            error: function (msg) {
                location.reload();
                exibirAviso(msg)
            }
        })
    }

    $(document).ready(function () {
        isAdmin()
    })
    loadAdmin = function () {
        $("#Content").load("/ERP/home/main/main.html");

        $("#sidebar").load("/ERP/home/sidebar/sidebar.html");

        carregaPrincipal = function () {
            console.log(checkSession())
            if (checkSession()) {
                $("#Content").load("/ERP/home/main/main.html")
            } else {
                location.reload()
            }
        }

        carregaVendas = function () {
            if (checkSession()) {
                $("#Content").load("/ERP/home/vendas/vendas.html")
            } else {
                location.reload()
            }

        }
        carregaEstoque = function () {
            if (checkSession()) {
                $("#Content").load("/ERP/home/estoque/estoque.html")
            } else {
                location.reload()
            }

        }
        carregaRelatorios = function () {
            if (checkSession()) {
                $("#Content").load("/ERP/home/relatorios/relatorios.html")
            } else {
                location.reload()
            }
        }
        carregaHistoricoVendas = function () {
            disableButtons()
            if (checkSession()) {
                $("#vendasContent").load("/ERP/home/vendas/historicoVendas.html")
            } else {
                location.reload()
            }
        }
        carregaUsuarios = function () {
            if (checkSession()) {
                $("#Content").load("/ERP/home/usuarios/main.html")
            } else {
                location.reload()
            }
        }
    }

    disableButtons = function () {
        document.getElementById("botoesVenda").hidden = true;
    }
    enableButtons = function () {
        document.getElementById("botoesVenda").hidden = false;
    }
    loadNormalUser = function () {
        $("#Content").load("/ERP/vendedor/index.html")
    }


    exibirAviso = function (aviso) {
        var modal = {
            title: "Mensagem",
            height: 250,
            width: 400,
            modal: true,
            buttons: {
                "OK": function () {
                    $(this).dialog("close");
                }
            }
        };
        $("#modalAviso").html(aviso);
        $("#modalAviso").dialog(modal);
    };
})

