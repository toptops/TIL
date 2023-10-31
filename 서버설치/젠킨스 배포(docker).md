# 젠킨스 배포 
- 기존 CI/CD는 보통 서버에 젠킨스를 올려서 배포했는데, 요번에는 도커에 올라가있는 젠킨스에서 dockerFile을 이용하여 image-conainer를 배포후 빌드까지를 진행하였다.
- 현재는 좀 야매로 해놨다... 차차 공부를 좀 더 깊숙히 진행하면서 해당 부분을 변경해야지......

## 도커 젠킨스 설정
- docker에 jenkins 이미지를 통해 컨테이너를 올릴때 DockerFile 또는 docker-compose 파일에 /var/run/docker.sock:/var/run/docker.sock 부분을 추가한다.
- /var/run/docker.sock는 Docker 데몬과 통신하기 위한 Unix 소켓 파일인데 해당 호스트 파일을 컨테이너에 마운트하여 사용하게 되면 예측해보건데 도커 클라이언트를 통해 발생하는 일련의 행동들이 도커 호스트 서버에 실행되는거 같다.
- 위 설정을 위해서는 jenkins 컨테이너 내부에도 도커를 설치해야 한다.


## 도커 파일 작성
- 이제 프로젝트 루트 혹은 도커 폴더를 만들어서 서버 이미지를 생성한다.(버전 관리 필요, 나는 일단은 안했다...!?)

## 젠킨스 Item 작성
1. 먼저 젠킨스 컨테이너에서 먼저 깃 파일들을 Pull 받는다.
2. 그래들 빌드를 통해 lib파일에 실행 가능한 jar파일이 생성된다.
3. 젠킨스 Execute Shell를 통해 명령어를 실행한다. 이때 Execute Shell 해당 item의 git 폴더의 최상위다.
4. 이후 Execute Shell에서 도커 이미지 생성 등등 일련의 행동을 하고 컨테이너를 실행해주는 작업을 추가해주면 이미지 및 컨테이너가 호스트와 공유되면서 호스트에서 도커에서 조회가 가능하다.
- 아래는 내가 야매로 만든 도커 이미지 빌드에서 컨테이너 생성까지, 진짜 너무 대충 짰지만 예상대로 동작은 잘한다!! 하하하....? 스크립트로 나중에 바꿔서 재대로 짜야겠다.
``` jenkins
docker stop my-container
docker rm my-container
docker rmi my-container:0.1
docker build -f ./docker/Dockerfile -t my-container:0.1 .
docker run --name my-container -p 8080:8080 -d my-container:0.1
```
