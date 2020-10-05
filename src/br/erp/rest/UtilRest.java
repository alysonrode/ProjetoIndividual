package br.erp.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;


public class UtilRest {
    public Response buildResponse(Object result) {
        try {
            //Ele começa retornando um status 200, que é o status de OK. Após isso, ele converte a resposta para JSON.
            String valorResposta = new Gson().toJson(result);
            return Response.ok(valorResposta).build();

        }catch(Exception ex){
            ex.printStackTrace();
//			Se algo acontecer fora do esperado, ele irá criar um Response de erro
            return this.buildErrorResponse(ex.getMessage());
        }
    }
    //Abaixo está o método responsável por enviar a resposta assim que qualquer erro acontecer.
//Nele temos a string str, que é a mensagem enviada pelo catch acima.
    public Response buildErrorResponse(String str) {
        //Abaixo o objeto rb recebe o status de erro
        ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//		Aqui é definido o objeto (mensagem) que será passado ao lado cliente.
        rb = rb.entity(str);

//		Define o tipo de retorno desta entidade(objeto), no
//		caso é definido como texto simples.

        rb.type("Text/plain");

//		Retorna o objeto de resposta com status 500 (erro)
//		juntamente com a String contendo a mensagem de erro

        return rb.build();
    }

}

