# NGINX 및 SSL 인증서 발급
- 가비아에 도메인을 구입후 DNS서버에 등록을 했다. www만 등록을 해놔서 지우고 다시 해야할꺼 같다. 허허
- certbot을 통해 ssl 인증서를 발급 받고 기존에 올라가있는 서버를 nginx 프록시 패스를 통해 프록시 서버로 사용할 예정이다.

## nginx 설치
- docker compose 파일을 작성하여 nginx:latest버전을 설치한다.
- 현재는 설치만 해놓고, 나머지는 직접들어가서 설정을 했다.. 나중에 dockerFile을 만들어서 적용시켜야지
- 아직 개념이 잘잡히지 않아서, 파일을 수동으로 볼륨으로 가져오고 있다...! 

``` docker
# docker-compose yml
version: '3.9'

services:
  nginx:
    image: nginx:latest
    container_name: nginx
    privileged: true
    ports:
    - 80:80
    - 443:443
    environment:
    - TZ=Asia/Seoul
    volumes:
    - /home/infra/nginx/config:/home/share
```

## nginx config 파일 설정
- conf.d 파일에 domain.conf을 작성한다
``` nginx
server {
    listen 80;
    server_name your_domain;
    location / {
    }
}
```
- 위와 같이 작성하고 nginx을 재시작해주면 1차 작업은 끝이난다.

## certbot 설치 및 ssl 인증서 적용
- certbot을 설치한다.
``` linux
apt-get update
apt-get install certbot
apt-get install python3-certbot-nginx
```
- 아래 명령어를 실행한다. 이때 80, 443포트를 미리 열어두어야 한다.
    - certbot certonly --nginx -d example.com -d www.example.com
- 이후 성공하게 되면 이전에 만들었던 domain.conf파일을 아래와 같이 수정하고 재시작하면 ssl 인증서 적용은 끝이다.
``` nginx
server {
    listen 80;
    server_name your_domain;
    location / {
        return 301 https://$host$request_uri;
    }
}

server {
    listen 443 ssl;
    server_name your_domain;
    
    ssl_certificate /etc/letsencrypt/live/your_domain/fullchain.pem
    ssl_certificate_key /etc/letsencrypt/live/your_domain/privkey.pem
    include /etc/letsencrypt/option-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparam.pem;

    location / {
        proxy_pass http://[서버주소]:[포트];
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host $http_host;
    }
}
```

## 개발 서버 구축
- docker를 통해서 jenkins와 mysql, redis를 설치하였고, 여기서 jenkins와 github을 연동하여 ci/cd를 구축하였다.
- 그리고 nginx를 통해서 프록시 서버를 구축하고 SSL 인증서 적용까지 끝났다!!!!
- 이제 개발 서버에 조금식 서버를 붙이면서 테스트 하면 되겠다... 하지만 아직 도커쪽이 좀 부실해서 좀 손좀 봐야겠다.
- 남은건 이제 모니터링과 검색엔진을 구축하는거만 남았다.
