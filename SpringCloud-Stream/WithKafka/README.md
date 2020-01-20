# SpringCloudStream with Kafka

### Zookeeper

- Single:  
 'zkServer.cmd'로 서버를 실행, 서버는 'conf/zoo.cfg'의 설정을 따른다.  
 이때 'zoo.cfg'의 'dataDir' 설정만 바꾸면 된다. 'dataDir'은 log가 쌓일 폴더이다.

- Multi:  
 홀수(n)개의 Zookeeper를 준비한다. n개의 'zkServer.cmd', 'conf/zoo.cfg'.  
 Single과 마찬가지로 'zoo.cfg'의 'dataDir'을 바꿔준다. 기왕이면 겹치지 n개의 서버 각각 폴더를 설정해 준다.  
 만약 하나의 컴퓨터로 여러대의 서버를 원한다면 'clientPort'를 각 서버가 겹치지 않게 지정하시면 됩니다.
 ```
    server.1=localhost:2888:3888
    server.2=localhost:2889:3889
    server.3=localhost:2890:3890
 ```
 그리고 위와 같은 설정 n개의 서버 모두에 추가합니다. 여기서 1,2,3은 각 서버의 id로 'dataDir'의 경로에 'myid' 파일의 내용을 참조합니다.  
 ___
 
 ### Kafka
 
 - Single  
   CLI를 이용하여 아래와 같이 실행시키면 된다. 'server.properties'에 필요한 설정들을 추가하면 됩니다. 'log.dirs'의 경로는 log를 저장할 폴더를 지정하므로 수정해주시면 됩니다.
 ```
    ${kafka}/bin/windows/kafka-server-start.bat ${kafka}/config/server.properties
 ```
 
 - Multi  
 'server.properties'를 n개 준비합니다. n개의 properties를 준비하고 각각의 properties에서 'broker.id', 'listeners', 'log/dirs', 'zookeeper.connect' 항목은 다르게 설정합니다.  
 그리곤 위의(Single) 실행 방법을 n번 반복합니다.(Ex. server1.properties, server2.properties,...)
 