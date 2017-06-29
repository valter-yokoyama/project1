Foram usados apache tommee ee6, jax-rs 2.0 ejb 3.1 mdb, activemq e mongodb

Motivações:
- activemq : gerenciador de fila de mensagens embutido no apache tommee, para suportar carga elevada
- apache tomme ee6: utilização de mdb do ejb 3.1 , mais fácil e claro de ser implementado. 
  É utilizado como consumer das mensagens, contém também as apis de jax-rs 2.0 implementadas
- jax-rs 2.0:  utilizado para se criar os micro servicos de consulta de protocolos do cliente
  e cadastro de manifestações. O padrão restful é muito mais simples de implementar e mais enxuto
  que SOAP. Estes serviços podem ser chamados pelo mobile, por javascript, por aplicações client.
- mongodb : utilizado para persistência das informações , para suportar carga elevada de escrita, 
  e é baseado em coleções.
  
Obs: 
     Não foi implementado nenhum mecanismo de autenticação (token, oauth, etc).
     Todas as ferramentas são opensource e de fácil instalação
     O uso da fila de mensagens se justifica em ambientes com uma carga muito grande e que tem que devolver uma resposta
     para o usuário, a geração do protocolo é feita de maneira assincrona e é enviada via email.
Servicos:
- inclusão de manifestação: http://localhost:8080/project/message/fulano?text=xxxxx&email=<conta email qualquer> 
- consulta de protocolos: http://localhost:8080/project/message/protocols/fulano
    

  
  