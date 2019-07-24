Producer-Consumer Pattern Test
=================================

클래스 설명
---------------------------------

MainClass : 입력파일경로, 결과파일경로, 병렬화 수를 입력받아 실행된다.

Partition : 하나의 BlockingQueue 를 가지며 put, poll 을 수행한다. 병렬화 개수 만큼 생성되며 Consumer 에 각각 전달되며 Producer 에는 partitionMap 에 담겨 전달된다.

Producer : 입력파일 경로와 partitionMap 을 생성자로 전달 받는다. 파일에서 각 라인을 읽어 유효성 확인 뒤, 데이터의 해쉬키를 이용하여 데이터를 각 파티션으로 전달한다.

Consumer : 전달받은 단어를 중복 확인하여 중복되지 않은 단어를 결과 파일 경로의 특정 파일에 쓴다.
