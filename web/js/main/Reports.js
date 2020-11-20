$(document).ready(function () {
    buildChart = function (Dates, Quant) {

        var ctx = document.getElementById('myChart');

        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: Dates,
                datasets: [{
                    label: 'Quantidade de vendas',
                    data: Quant
                }]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
    }

    getDatas = function () {
        var from = document.getElementById("dataInicial").value;
        var to = document.getElementById("dataFinal").value;

        $.ajax({
            url: "/ERP/rest/vendas/getDataToChat",
            type: "GET",
            data: "from=" + from + "&to=" + to,
            success: function (chartData) {
                buildChart(chartData.datas, chartData.quantidades);
            },
            error: function () {

            }
        })
    }

    getDatesInterval = function () {
        var from = document.getElementById("dataInicial").value;
        var to = document.getElementById("dataFinal").value;

        if ((from == "" || from == undefined) && (to == "" || to == undefined)) {

            var today = new Date();

            var day = today.getDate().toString().length < 2 ? "0" + today.getDate() : today.getDate()

            var month = (today.getMonth() + 1).toString().length < 2 ? "0" + (today.getMonth() + 1) : (today.getMonth() + 1)


            from = "01/" + month + "/" + today.getFullYear();
            to = day + "/" + month + "/" + today.getFullYear();
        }

        return [from, to];

    }

    getDataToPDF = function () {
        var from = document.getElementById("dataInicial").value;
        var to = document.getElementById("dataFinal").value;

        $.ajax({
            url: "/ERP/rest/vendas/getDataToPDF",
            type: "GET",
            data: "from=" + from + "&to=" + to,
            success: function (Sells) {
                generateHTMLtoPDF(Sells)
            },
            error: function () {

            }
        })
    }

    generateHTMLtoPDF = function (Sells) {

        var datas = getDatesInterval()
        var from = datas[0];
        var to = datas[1];
        $("#logo-relatorio").html("Odin")
        $("#data-relatorio-gerado").html("Relatório gerado em: " + pegaDataAtual());
        $("#intervalo-relatorio").html("Vendas entre: " + from + " e " + to)

        // >"
        var html =
        "  <tr>\n" +
        "    <th>Data venda</th>\n" +
        "    <th>Valor total</th>\n" +
        "    <th>Vendedor</th>\n" +
        "  </tr>\n"

        var valorTotal = 0;

        for(var i = 0; i < Sells.length ; i++){

            //html += "<fieldset>"// class='border border-secondary'>"
            html+= "<tr>"
            html += "<th>"+ Sells[i].dataVenda +"</th>"
            html += "<th>"+ moneyMask(Sells[i].valorTotal) +"</th>"
            html += "<th>"+ Sells[i].usuario.firstName+ " " +Sells[i].usuario.lastName  + "</th>"
            html += "</tr>"
            html += "<hr>"

            valorTotal = valorTotal + Sells[i].valorTotal;

        }
        $("#valor-total-relatorio").html("VALOR TOTAL: " + moneyMask(valorTotal))

        $("#fieldset-vendas").html(html);
        openModalReport()

    }

    openModalReport = function () {
        var modal = {
            title: "Mensagem",
            height: 650,
            width: 800,
            modal: true,
            buttons: {
                "Download": function () {
                    generatePDF()
                    $(this).dialog("close");
                },
                "Cancelar": function () {
                    $(this).dialog("close");
                }
            }
        };
        $("#pdf-to-generate").dialog(modal);
    }

    generatePDF = function () {
        html2canvas($("#pdf-to-generate"), {
            onrendered: function (canvas) {
                var imgData = canvas.toDataURL(
                    'image/png');
                var doc = new jsPDF('p', 'mm');
                doc.addImage(imgData, 'PNG', 1, 15);
                doc.save('relatório.pdf');
            }
        })
    }
})