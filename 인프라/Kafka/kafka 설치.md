# kafka 설치

## kafka 설치
- 고가용성을 테스트해보기 위해 (카프카 브로커 + 주키퍼)*3 대로 클러스터 구성
- 모니터링 서버 그라파나와 연동하여 구성 + 카프카 매니저 이용해볼 예정

## kafka 서버 구성
- zookeeper 3대 + 브로커 서버 3대 구성완료(클러스터 구성)
- 디테일한 설정은 일단 제외했다.

## kafka admin ui
- kafka admin ui는 아파치 카프카 admin ui로 구성
- kafka 설정 중에 '***KAFKA_ADVERTISED_LISTENERS***' 이부분에서 localhost가 아닌 실제 ip주소를 입력해줘야 한다.
    - 진짜 여기서 3시간 해멨다. 깃 공홈에서 찾아서 처리했다 ㅠㅠ

## kafka cluster
- prometous + grafana 