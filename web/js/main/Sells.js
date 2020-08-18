showProducts = function (){

}

createLine = function () {
    var form = document.getElementById("formVendas")
    form.appendChild(productLine())
}
productLine = function () {

    var count = getId()
    var divSub = document.createElement('div')
    divSub.setAttribute('class',"form-row inputsCadastro2")
    var divProduto = document.createElement('div')
    divProduto.setAttribute('id', 'produtoVenda' + count)
    var inputProduto = document.createElement('input')
    inputProduto.setAttribute('type','text')
    inputProduto.setAttribute('class','form-control')
    inputProduto.setAttribute('id','inputProduto' + count)
    inputProduto.setAttribute('placeholder','Nome ou id do prod.')
    var divQuantidade = document.createElement('div')
    divQuantidade.setAttribute('id','quantidadeVenda' + count)
    var inputQuantidade = document.createElement('input')
    inputQuantidade.setAttribute('type', 'number')
    inputQuantidade.setAttribute('class', 'form-control')
    inputQuantidade.setAttribute('id', 'inputQuantidade' + count)
    inputQuantidade.setAttribute('placeholder', 'Quantidade')
    var divValorUnit = document.createElement('div')
    divValorUnit.setAttribute('id','valorUnitVenda'+count)
    var inputValorUnit = document.createElement('input')
    inputValorUnit.setAttribute('type','text')
    inputValorUnit.setAttribute('class','form-control')
    inputValorUnit.setAttribute('id','inputValorUnit' + count)
    inputValorUnit.setAttribute('placeholder','R$####,00')
    var divValorTotal = document.createElement('div')
    divValorTotal.setAttribute('id','inputValorTotal' + count)
    var inputValorTotal = document.createElement('input')
    inputValorTotal.setAttribute('type','text')
    inputValorTotal.setAttribute('class','form-control')
    inputValorTotal.setAttribute('id','inputValorTotal' + count)
    inputValorTotal.setAttribute('placeholder','')
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