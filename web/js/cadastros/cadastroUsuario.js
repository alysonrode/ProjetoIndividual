cadastroUsuario = function () {
    var modalCadastraUsuario = {
        height: 550,
        width: 700,
        modal: true,
        buttons: {
            "Salvar": function () {
                validaCampos()
            },
            "Cancelar": function () {
                $(this).dialog("close")
            }
        }
    }
    $("#modalCadastroUsuário").dialog(modalCadastraUsuario);
}
validaSenha = function () {
    var senha = document.frmCadastraUsuario.password.value;
    var confirmacaoSenha = document.frmCadastraUsuario.password_confirmation.value;
    var authentication;

    if (senha == confirmacaoSenha) {
        return true;
    }
    else {
        var modalSenhaIncopativeis = {
            height: 150,
            width: 500,
            modal: true,
        }
        exibirAviso("As senhas são incompatíveis!");
        return false;
    }
}
validaCampos = function () {
    user = new Object()

    user.firstName = document.frmCadastraUsuario.first_name.value;
    user.lastName = document.frmCadastraUsuario.last_name.value;
    user.email = document.frmCadastraUsuario.emailUsuario.value;
    user.senha = document.frmCadastraUsuario.password.value;
    user.administrador = document.frmCadastraUsuario.adminstrador.checked;
    user.dataNasc = document.frmCadastraUsuario.dataNasc.value;

    if ((user.firstName == "") || (user.lastName == "") || (user.senha == "")
        || (user.confirmacaoSenha == "") || (user.email == "")) {
        exibirAviso("Preencha todos os campos!");
    }
    else{
        var senhaValida = validaSenha()
        if (senhaValida){
            cadastrar(user)
        }
    }
}

cadastrar = function(user){
    $.ajax({
        type: "POST",
        url: "/ERP/rest/usuarios/cadastrar",
        data: JSON.stringify(user),
        success : function (msg) {
            exibirAviso(msg)
        },
        error : function (msg) {
        }
    })
}

buscar = function () {
    var busca = document.frmBuscaUsuarios.buscarUsuarios.value;
    $.ajax({
        type: "GET",
        url: "/ERP/rest/usuarios/buscar",
        data: "valorBusca=" +busca,
        success: function (listUsers) {
            console.log(listUsers)
        }
    })
}