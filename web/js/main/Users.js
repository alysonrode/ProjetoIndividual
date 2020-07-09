cadastroUsuario = function () {

    $.ajax({
        type: "POST",
        url: "/ERP/rest/usuarios/buscaid",
        success : function(id){
            document.frmUsuario.matricula.value = id;
            var modalCadastraUsuario = {
                height: 550,
                width: 700,
                modal: true,
                buttons: {
                    "Salvar": function () {
                        var result = validaCampos()
                        if(result)
                            $(this).dialog("close")
                            document.getElementById("frmUsuario").reset()

                    },
                    "Cancelar": function () {
                        document.getElementById("frmUsuario").reset()
                        $(this).dialog("close")
                    }
                }
            }
            $("#modalUsuarios").dialog(modalCadastraUsuario);


        }
    })

}
validaSenha = function () {
    var senha = document.frmUsuario.password.value;
    var confirmacaoSenha = document.frmUsuario.password_confirmation.value;
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
validaCampos = function (atualizar) {
    user = new Object()

    user.firstName = document.frmUsuario.first_name.value;
    user.lastName = document.frmUsuario.last_name.value;
    user.email = document.frmUsuario.emailUsuario.value;
    user.senha = document.frmUsuario.password.value;
    user.administrador = document.frmUsuario.adminstrador.checked;
    user.dataNasc = document.frmUsuario.dataNasc.value;
    user.id = document.frmUsuario.matricula.value;

    if((user.firstName == "") || (user.lastName == "") || (user.email == "")){
        exibirAviso("Preencha todos os campos!");
        return false;
    }
    if(!atualizar && (user.senha == "") || (user.confirmacaoSenha == "")) {
        exibirAviso("Preencha as senhas!");
        return false;
    }
    if((atualizar && (!user.senha == "") && (user.confirmacaoSenha == ""))
        || (atualizar && (user.senha == "") && (!user.confirmacaoSenha == "")) ) {
        exibirAviso("Preencha as senhas corretamente!");
        return false;
    }
    else{
        var senhaValida = validaSenha()
        if (senhaValida){
            if (atualizar){
                registraAtualizacao(user)
            }else{
                cadastrar(user)
            }
            return true;
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
            buscar();
        },
        error : function (msg) {
        }
    })
}
registraAtualizacao = function(user){
    $.ajax({
        url: "/ERP/rest/usuarios/atualizaUsuario",
        type: "POST",
        data: JSON.stringify(user),
        success : function (msg) {
            exibirAviso(msg);
            buscar();
        },
        error : function (msg) {
            exibirAviso(msg)
        }
    })
}

buscar = function () {
    var busca = document.getElementById("buscarUsuarios").value;
    $.ajax({
        type: "GET",
        url: "/ERP/rest/usuarios/buscar",
        data: "valorBusca=" +busca,
        success: function (listUsers) {
             $("#tabelaUsuarios").html(
                    montaHtml(listUsers))
        }
    })
}
montaHtml = function (Usuarios) {

    var table = "<table class=\"table user-list\">" + "<thead>" + "<tr>"
                + "<th><span>Usuário</span></th>" + "<th> Nível </th>" + "<th><span>Status</span></th>"
                + "<th><span>E-mail</span></th>" + "<th id='acoes'>Ações</th>" + "</tr>" + "</thead>" + "<tbody id=\"listaUsuarios\">"
    if(Usuarios == undefined || Usuarios.length < 1 ){
        table += "<h3> Nenhum usuário encontrado </h3>"
    }
    else{
        for (var i = 0; i < Usuarios.length; i++){
            var admin = "Normal"
            if (Usuarios[i].administrador){
                admin = "Administrador"
            }
            table += "<tr>" + "<td>" + "<span>" + Usuarios[i].firstName + " " + Usuarios[i].lastName + "</span>" + "</td>"
                   + "<td>" + "<span>" + admin + "</span>" + "</td>"
                   + "<td>" + "<span>" + "Ativo" + "</span>" + "</td>"
                   + "<td>" + "<span>" + Usuarios[i].email + "</span>" + "</td>"
                   + "<input type='hidden' value='" + Usuarios[i].id + "' id='idUsuario='"+Usuarios[i].id + "></input>" + "<td>\n" +

            "</a>\n" +
            "<a href=\"#\" class=\"table-link\">\n" +
            "\t\t\t\t\t\t\t\t\t<span class=\"fa-stack\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<i class=\"fa fa-pencil\" onclick='AtualizaUsuario("+ Usuarios[i].id +")'></i>\n" +
            "\t\t\t\t\t\t\t\t\t</span>\n" +
            "</a>\n" +
            "<a href=\"#\" class=\"table-link danger\">\n" +
            "\t\t\t\t\t\t\t\t\t<span class=\"fa-stack\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<i class=\"fa fa-trash-o\"onclick='deletarUsuario("+ Usuarios[i].id + ")'></i>\n" +
            "\t\t\t\t\t\t\t\t\t</span>\n" +
            "</a>\n" +
            "</td>"

            + "</tr>"
        }
    }
    table += "</tbody>" + "<table>"

    return table;
}

AtualizaUsuario = function(id){
    console.log(id)
    $.ajax({
        type: "GET",
        url: "/ERP/rest/usuarios/buscaPorId",
        data: "id=" + id,
        success: function (Usuario) {
            document.frmUsuario.first_name.value = Usuario.firstName;
            document.frmUsuario.last_name.value = Usuario.lastName;
            document.frmUsuario.emailUsuario.value = Usuario.email;
            document.frmUsuario.matricula.value = Usuario.id;
            document.frmUsuario.dataNasc.value = Usuario.dataNasc;
            var admin = Usuario.administrador;
            if (admin) {
                document.frmUsuario.adminstrador.checked = true;
            }

            var modalAtualizaUsuario = {
                height: 550,
                width: 700,
                modal: true,
                buttons: {
                    "Salvar": function () {
                        var actualizes = true
                        var result = validaCampos(actualizes)
                        if (result)
                            $(this).dialog("close")
                            document.getElementById("frmUsuario").reset()

                    },
                    "Cancelar": function () {
                        document.getElementById("frmUsuario").reset()
                        $(this).dialog("close")
                    }
                }
            }
            $("#modalUsuarios").dialog(modalAtualizaUsuario);
        }
    })
}
deletarUsuario = function (id) {
    var modalConfirmacao={
        title: "Confirmação",
        height: 250,
        width: 400,
        modal: true,
        buttons: {
            "SIM": function(){
                $.ajax({
                    url:"/ERP/rest/usuarios/deletaUsuario",
                    type: "GET",
                    data: "id=" +id,
                    success: function (msg) {
                        buscar()
                        exibirAviso(msg)
                    },
                    error : function (msg) {
                        exibirAviso(msg)
                    }
                })

                $(this).dialog("close");
            },
            "NÃO": function () {
                $(this).dialog("close");
            }
        }
    }
    $("#modalExclusao").dialog(modalConfirmacao)
}
