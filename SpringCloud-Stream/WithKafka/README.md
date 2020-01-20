# SpringCloudStream with Kafka

### Zookeeper

- Single:\
 'zkServer.cmd'로 서버를 실행, 서버는 'conf/zoo.cfg'의 설정을 따른다.  
 이때 'zoo.cfg'의 'dataDir' 설정만 바꾸면 된다. 'dataDir'은 log가 쌓일 폴더이다.

- Multi:\
 홀수(n)개의 Zookeeper를 준비한다. n개의 'zkServer.cmd', 'conf/zoo.cfg'.  
 Single과 마찬가지로 'zoo.cfg'의 'dataDir'을 바꿔준다. 기왕이면 겹치지 n개의 서버 각각 폴더를 설정해 준다.
 ```
    server.1=localhost:2888:3888
    server.2=localhost:2889:3889
    server.3=localhost:2890:3890
 ```
 그리고 위와 같은 설정 n개의 서버 모두에 추가합니다. 여기서 1,2,3은 각 서버의 id로 'dataDir'의 경로에 'myid' 파일의 내용을 참조합니다.  
 

