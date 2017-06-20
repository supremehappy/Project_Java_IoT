* src\DBClient 폴더 = Client, Server 간의 1:1(일대일) 소켓통신 / Server는 DB에 접속하여 조회 및 수정하여 데이터 송수신, View는 DB에 접속하여 조회하여 데이터출력
* src\MultiClient 폴더 = Client, View, Server 간의 2(Client,View):1(다대일) 통신 / Server는 DB에 접속하여 조회 및 수정하여 데이터 송수신 / 

* Client는 입력값들을 Server 에게 소켓통신으로 전송. Server에게서 받은 데이터값을 화면에 출력.
* Server는 Client에게 받은 데이터값들을 기반으로 DB에 접속하여 조회하거나 수정. 조회된 데이터들을 Client 나 View 에게 송신.
* View는 Server에게 입력값을 송신한 뒤, Server에게서 받은 데이터값을 화면에 출력.

