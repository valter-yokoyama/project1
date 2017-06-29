Foram usados apache tommee ee6, jax-rs 2.0 ejb 3.1 mdb, activemq e mongodb

Motiva��es:
- activemq : gerenciador de fila de mensagens embutido no apache tommee, para suportar carga elevada
- apache tomme ee6: utiliza��o de mdb do ejb 3.1 , mais f�cil e claro de ser implementado. 
  � utilizado como consumer das mensagens, cont�m tamb�m as apis de jax-rs 2.0 implementadas
- jax-rs 2.0:  utilizado para se criar os micro servicos de consulta de protocolos do cliente
  e cadastro de manifesta��es. O padr�o restful � muito mais simples de implementar e mais enxuto
  que SOAP. Estes servi�os podem ser chamados pelo mobile, por javascript, por aplica��es client.
- mongodb : utilizado para persist�ncia das informa��es , para suportar carga elevada de escrita, 
  e � baseado em cole��es.
  
Obs: 
     N�o foi implementado nenhum mecanismo de autentica��o (token, oauth, etc).
     Todas as ferramentas s�o opensource e de f�cil instala��o
     O uso da fila de mensagens se justifica em ambientes com uma carga muito grande e que tem que devolver uma resposta
     para o usu�rio, a gera��o do protocolo � feita de maneira assincrona e � enviada via email.
Servicos:
- inclus�o de manifesta��o: http://localhost:8080/project/message/fulano?text=xxxxx&email=<conta email qualquer> 
- consulta de protocolos: http://localhost:8080/project/message/protocols/fulano
    

  
  