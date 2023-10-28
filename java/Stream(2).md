# Stream 함수 정리

## 자바 버전 9이상에서 Stream.of() 를 사용할 수 있습니다.
## 2. Java Stream 함수
1. Stream 만들기
    1. 배열
        - 배열의 경우 Arrays.stream 메소드를 사용하거나 배열 -> 컬렉션으로 변경하여 스트림을 생성할 수 있습니다.
        ~~~ java
            String arr[] = {"a", "b", "c"};
            Stream<String> stream = Stream.of(arr);
            Stream<String> stream = Arrays.stream(arr);
            // 인덱스 범위 지정하여 스트림 생성
            Stream<String> stream = Arrays.stream(arr, 0, 1); 
        ~~~
    2. 컬렉션
        - 컬렉션의 경우 각 구현 클래스의 stream 메서드를 이용하여 스트림을 생성할 수 있습니다.
        ~~~ java
            List<String> list = Arrays.asList("A","B","C");
            Stream<String> stream = list.stream();

            // Collection 인터페이스내 추가된 stream 함수
            public interface Collection<E> extends Iterable<E> {
                default Stream<E> stream {
                    return StreamSupport.stream(spliterator(), false);
                }
                ...
            }
        ~~~
    3. 기본 타입형 스트림
        - 제네릭을 사용하여 컬렉션 혹은 배열을 이용하여 기본타입 스트림을 생성할 수 있습니다.
        - 제네릭을 사용하지 않고 기본타입에 특화된 스트림을 사용할 수 있다.(비효율적인 오토박싱/언박싱을 사용하지 않음)
        - 랜덤 함수를 통해서 난수 스트림을 생성할 수 있다. 단, 무한스트림으로 생성되기 때문에 limit등을 통해 크기를 지정해야 한다.
        ~~~ java
            // 제네릭을 사용한 기본 타입 스트림
            List<Integer> list = Arrays...
            Stream<Integer> stream = list.stream();

            // 기본 타입에 특화된 스트림
            IntStream intStream = IntStream.range(0, 3);
            LongStream longStream = LongStream.range(0, 3);
            DoubleStream doubleStream = DoubleStream.range(0, 3);

            // 기본 타입에 특화된 스트림에서 박싱이 필요할 경우, 제공되는 boxed() 함수 사용
            Stream<Integer> boxedStream = IntStream.range(0, 3).boxed();

            // 난수 스트림
            IntStream intStream = new Random.ints().limit(3);
            DoubleStream doubleStream = new Random.doubles(3);
        ~~~
    4. 문자열 스트림
        - 문자열을 이용해서 스트림을 생성할 수 있지만 각 스트림의 문자를 이용해서 IntStream으로 생성할 수도 있다.
        - 또한 정규표현식을 이용해서 문자열을 자른후 각 요소들로 스트림을 만들수도 있다.
        ~~~ java
            // 문자열의 각 요소인 문자를 이용하여 IntStream 생성
            IntStream charStream = "Test".chars();

            // 정규표현식을 이용한 스트림 생성
            Stream<String> regStream = Pattern.compile(",").splitAsStream("A,b,c");
        ~~~
    5. 빈 스트림
        - 스트림 요소가 존재하지 않는 빈스트림을 생성할 수 있다. null 대신 사용할 수 있으며 유효성 검사에서 사용된다.
        ~~~ java
            Stream<Object> empty = Stream.empty();
        ~~~
    6. 파일 스트림
        - JAVA NIO의 Files 클래스를 이용하여 스트림을 생성할 수 있다.
        ~~~ java
            // java.nio.Files 클래스를 이용하여 생성
            Path path = Paths.get("~");
            Stream<Path> list = Files.list(path);

            Path path = Paths.get("~.txt");
            Stream<String> lines = Files.lines(path);
        ~~~
    7. 버퍼리더 스트림
        - JAVA IO 클래스의 BufferedReader 클래스의 lines 메서드로 문자열 스트림을 생성할 수 있다.
        ~~~ java
            // java.io.BufferedReader 클래스를 이용하여 생성
            BufferedReader br = new BufferedReade(new FileReader("test.txt"));
            Stream<String> stream = br.lines();
        ~~~
    8. 병렬 스트림
        - 기본적으로 스트림은 직렬이므로 병렬 스트림을 위해서는 stream 대신 parallelStream() 선언하여 간단히 생성이 가능하다.
        - 병렬 스트림에서 직렬 스트림으로 변경하려면 sequential()을 사용하면 된다.
        ~~~ java
            List<String> list = new ArrayList<>();
            ...
            // 병렬 스트림 생성
            Stream<String> parallelStream = list.parallelStream();
            // 병렬 스트림인지 확인
            Stream<String> sequentialStream = parallelStream.sequential();

            // 병렬 스트림을 직렬 스트림으로 생성, 스트림은 한번 사용되면 닫히므로 새로 생성해야 한다.
            boolean isParallel = parallelStream.isParallel();
        ~~~
    9.  두 스트림 연결
        - 스트림은 Stream.concat 메소드를 이용하여 두 개의 스트림을 연결하여 새로운 스트림을 생성할 수 있다.
        ~~~ java
            Stream<String> stream1 = Stream.of("A", "B", "C");
            Stream<String> stream2 = Stream.of("a", "b", "c");
            Stream<String> concat = Stream.concat(stream1, stream2);
            // [A, B, C, a, b, c]
        ~~~
    10. Stream 제공 함수
        1. Stream.builder()
           - Stream 클래스의 빌더 함수을 이용하면 빌더 패턴을 통해 스트림에 직접 원하는 값을 넣어 생성할 수 있습니다.
        2. Stream.generate()
           - Stream에서 제공하는 함수로 람다식(Supplier<T>)을 매개변수로 받아 스트림을 생성합니다.
           - 람다식(Supplier<T>)를 통해 초기값을 생성하지만 이전 결과에 대해 영향이 끼치지 않으므로 독립적입니다.
           - 무한 스트림으로 생성되기 때문에 적절히 크기를 제한해야 합니다.
        3. Stream.iterate()
            - Stream에서 제공하는 함수로 람다를 이용하여 스트림에 들어갈 요소를 만듭니다.
            - 초기 값과 람다를 인수로 받아 생성하는게 특징이며 이전 결과에 종속적입니다.
            - 이 함수 또한 무한 스트림으로 생성되기 적절히 크기를 제한해야 합니다.
        ~~~ java
            // Stream.builder() 함수를 통해 생성
            Stream<String> stream = Stream.<String>builder().add("A").add("B").build();

            // Stream.generate() 함수를 통해 생성
            Stream<String> stream = Stream.generate(() -> "A").limit(5);
            Stream<Integer> random = Stream.generate(Math::random).limit(5);

            // Stream.iterate() 함수를 통해 생성
            Stream<Integer> stream = Stream.iterate(1, n -> n+1).limit(5);
            // [1, 2, 3, 4, 5]
        ~~~
2. Stream 함수
    1. 중간 연산
        - 중간 연산은 스트림 생성이후 필터링이나 원하는 형태에 맞게 가공하는 작업을 의미한다. 
        - 특징으로는 반환 값이 다른 스트림을 반환하기 때문에 메서드 체이닝이 가능하며 각 중간연산의 작업들은 최종 연산을 시작하게되면 그때 적용되어 처리합니다. 즉, 최종연산에서 메서드 체이닝이 끝나게 되고 실제 중간연산의 처리가 시작되는 것 입니다.(지연 연산)
        - 즉, 반드시 최종연산 코드를 넣어야 결과를 확인할 수 있다.
        1. 스트림 필터링
            - 스트림 요소들을 필터링하기 위한 메서드
            1.  filter
                - 스트림 내 각 요소들을 조건에 맞게 필터링하는 함수입니다.
                - 메서드 인자로 함수형 인터페이스 Predicate<T>로 받으며 test()라는 추상메소드가 정의되어 있고 해당 메소드를 통해 filter 평가식을 처리 합니다.
                ~~~ java
                    List<String> list = List.of("A", "B", "C");
                    // 일반적인 filter  
                    List<String> stream = list.stream()
                                                .filter(s -> s != null)
                                                .collect(Collectors.toList());

                    // Predicate를 직접 구현한 filter 예제
                    List<String> stream = list.stream().filter(new Predicate<String>{
                                                                    @Override
                                                                    public boolean test(String s) {
                                                                        return s != null;
                                                                    }
                                                                }).collect(Collectors.toList());
                ~~~ 
            2.  distinct
                - distinct 메서드는 이름 그대로 스트림 요소내 중복을 제거합니다. 
                - 특징적인 점은 기본형 타입일 경우 value를 비교하지만 객체일 경우 equals()메서드로 비교합니다. 올바른 값을 얻기 위해서는 equals()를 오버라이딩해서 재정의가 필요합니다.
                ~~~ java
                    class test {
                        private int testInt;
                        public test(int testInt) {
                            this.testInf = testInt;
                        }
                    }

                    public static void main(String args[]) {
                        // 기본 타입형 비교
                        IntStream stream = Arrays.stream(new int[] {1, 2, 3, 4});
                        IntStream distinctStream = stream.distinct();

                        // 객체 비교
                        test test1 = new test(1);
                        test test2 = new test(2);
                        List<test> list = List.of(test1, test2, test1);

                        List<test> distinctStream = list.stream.distinct()
                                                        .collect(Collectors.toList());;
                    }
                ~~~
        2. 스트림 제한
            - 스트림 요소를 제한하기 위한 메서드
            1. limit
                - 스트림 내 요소 개수를 제한할 수 있습니다. 즉, 스트림 개수를 지정한다고 보면 된다.
                ~~~ java
                    List<String> list = List.of("A", "B", "C")
                                            .stream()
                                            .limit(2)
                                            .collect(Collectors.toList());
                    // [A, B]
                ~~~
            2. skip
                - 스트림 내의 첫번쨰 요소부터 인자로 전달된 개수 만큼 요소를 제외
                ~~~ java
                    List<String> list = List.of("A", "B", "C")
                                            .stream()
                                            .skip(2)
                                            .collect(Collectors.toList());;
                    // [C]
                ~~~
        3. 스트림 결과 확인
            1. peek
                - 스트림 내 요소들 각각을 대상으로 특정 연산을 수행하는 메소드이다.
                - 특징적으로는 요소들을 소모해 특정 결과를 반환하지 않는 Consumer를 인자로 받고 있어서 보통 결과를 확인하는데 사용합니다.
                ~~~ java
                    int sum = IntStream.of(1, 2, 3, 4, 5)
                                    .peek(System.out::println)
                                    .sum();
                    // Stream요소인 1~5 출력
                ~~~
        4. 스트림 정렬
            1. sorted
                - 스트림 내 요소들을 정렬하기 위해 사용됩니다.
                ~~~ java
                    // 컬렉션 스트림 정렬
                    List<Integer> sorted = List.of(1, 3, 5, 2, 4)
                                                    .stream()
                                                    .sorted()
                                                    .collect(Collectors.toList());;
                    // 컬렉션 스트림 역순 정렬
                    List<Integer> sorted = List.of(1, 3, 5, 2, 4)
                                                    .stream()
                                                    .sorted(Comparator.reversOrder())
                                                    .collect(Collectors.toList());;

                    // 기본 타입형 스트림의 경우 sorted 메서드에 인자를 넘길때 boxed메서드를 이용해 변환 후 사용해야 한다.
                    List<Integer> sorted = IntStream.of(1, 3, 5, 2, 4)
                                                    .boxed()
                                                    .sorted()
                                                    .collect(Collectors.toList());;
                ~~~
        5. 스트림 변환
            1. map
                - 보통 스트림 요소중 특정 요소나 혹은 형태로 변환해야 될때 사용된다.
                - 매개변수로 Function<? super T, ? extends R>을 받는데 이때 T는 input데이터이고 R은 Return 데이터이다. 즉, 스트림 요소의 값을 받아 새로운 타입으로 변환하여 새로운 스트림에 담는 작업이다. 이를 Mapping이라고 한다.
                ~~~ java
                    // 일반적인 매핑
                    List<String> stream = List.of("a", "b", "c")
                                                .stream()
                                                .map(s -> s.toUpperCase());
                                                // .map(String::toUpperCase); 위 코드는 이렇게도 줄일수 있다.
                                                .collect(Collectors.toList());
                    
                    // Function을 함수로 받아서 정의할때
                    // Function함수의 2개의 제네릭 인자는 앞에서부터 input 데이터 타입과 return 데이터 타입이다.
                    Function<String, String> func = new Function<String, String>() {
                        @Override
			            public String apply(String t) {
                            return t.toUpperCase();
                        }
                    };

                    List<String> stream = List.of("a", "b", "c")
                                                .stream()
                                                .map(s -> func.apply(s));
                                                .collect(Collectors.toList());
                ~~~
            2. flatmap
                - 보통 중첩된 구조를 한단계 없애고 단일 원소 스트림으로 만들어준다.
                - 스트림 요소가 배열형태일때 주로 사용된다.
                ~~~ java
                    // 배열일 경우
                    String[][] arr2 = new String[][] {
                        {"a", "A"}, {"b", "B"}, {"c", "C"}
                    };

                    List<String> stream = Arrays.stream(arr2)
                                                .flatMap(arr1 -> Arrays.stream(arr1))
                                                .collect(Collectors.toList());

                    // 배열일 경우 Function 선언의 경우
                    Function<String[], Stream<String>> func = new Function<String[], Stream<String>>() {
                        @Override
                        public Stream<String> apply(String[] t) {
                            return Arrays.stream(t);
                        }
                    }

                    List<String> stream = Arrays.stream(arr2)
                                                .flatMap(arr1 -> func.apply(arr1))
                                                .collect(Collectors.toList());

                    // List형일 경우
                    List<String> list1 = List.of("a", "A");
                    List<String> list2 = List.of("b", "B");
                    List<String> list3 = List.of("c", "C");
                    List<List<String>> all = List.of(list1, list2, list3);

                    List<String> stream = all.stream()
                                            .flatMap(list -> list.stream())
                                            .collect(Collectors.toList());
                ~~~
        6. 기본 타입 특화된 스트림 변환
            - 기본 타입들은 boxed이라는 작업이 필요한데 이를 사용하지 않는 기본 타입에 특화된 스트림 변환을 할 수 있다.
            ~~~ java
                // 기본 타입 중 Int에 특화된 스트림 변환(Long, Double형은 Type만 다를뿐 메커니즘은 같다.)
                List<String> list = List.of("1", "2", "3");
                List<Integer> convert = list.stream()
                                            .mapToInt(value -> Integer.valueOf(value))
                                            .collect(Collectors.toList());
                // [1, 2, 3]
                // mapToInt의 경우 매개변수 인자로 ToIntFunction<T>를 받는데 이를 구현할 경우
                ToIntFunction<String> func = new ToIntFunction<String>() {
			        @Override
			        public int applyAsInt(String value) {
				        return Integer.valurOf(value);
                    }
                };
			
                List<String> list = List.of("1", "2", "3");
                List<Integer> convert = list.stream()
                                            .mapToInt(value -> func.applyAsInt(value))
                                            .collect(Collectors.toList());
            ~~~
    2. 최종 연산
        - 중간 연산자를 통해 가공한 데이터를 결과 값으로 만든다. 따라서 스트림을 끝내는 최종 작업이다.
        - 최종연산의 결과로 Optional<T> 클래스가 등장하는데 해당 클래스는 'T'타입의 객체를 포장해주는 래퍼 클래스이다. 인스턴스는 모든 타입의 참조변수를 저장할 수 있다.
        - Optional<T>에서는 여러가지 편리한 함수를 제공하여 결과처리에 도움을 준다. 단적인 예로 매번 null처리가 아닌 제공되는 함수를 통해 에러나 다른 값으로 변환할수도 있다.
        ~~~ java
            // Optional<T> 예
            Optional<String> optVal = Optional.of("abc");
            
            // optVal에 저장된 값이 null일시 ""을 리턴한다.
            String str = optVal.orElse("");
            // 람다식도 사용 가능
            String str = optVal.orElseGet(String::new);
            // 예외도 발생 가능
            String str = optVal.orElseThrow(NullPointerException::new); 
        ~~~
        1. 요소의 순회 
            1. foreach
                - for문과 동일하게 요소들을 순회할수 있다.
                - 단, 병렬 스트림에서는 요소들을 순회할때 순서를 보장할 수 없다. 순서를 보장하고 싶다면 forEachOrdered() 메서드를 사용하면 된다.
                ~~~ java

                    List<Integer> list = List.of(3, 2, 1, 5, 7);
                    // forEach 구문을 통한 스트림 요소 출력
                    list.stream().forEach(System.out::println);
                    
                    // 병렬 스트림에서 그냥 forEach로 할 경우 매 실행마다 출력결과가 다르다.
                    list.parallelStream().forEach(System.out::println);

                    // forEachOrdered로 변경할 경우 매 실행마다 출력결과가 같아진다.
                    list.parallelStream().forEachOrdered(System.out::println);
                ~~~
        2. 요소의 집계함수
            - 스트림이 비어있는 경우 에러를 발생시킬수 있지만 Optional<T>를 리턴하기 때문에 null 체크를 간단히 할 수 있다.
            - 단, 기본값에 특화된 stream(IntStream(), LongStream(), DoubleStream())에 한해서만 기본적으로 제공된다.
            ~~~ java
                
                List<Integer> list = List.of(1, 2, 3, 4, 5);

                // sum()
                // sum의 경우에는 Stream<Integer>의 경우에도 지원이 안된다.
                int sum = IntStream.of(1, 2, 3, 4, 5).sum();

                // count()는 Type은 long이다.
                long count = IntStream.of(1, 2, 3, 4, 5).count();
                // Stream<Integer>의 경우 지원이 된다.
                int count = list.stream().filter(num -> num > 0).count();

                // max, min 기본 타입 특화된 스트림의 경우
                OptionalInt min = IntStream.of(1, 2, 3, 4, 5).min();
                OptionalInt max = IntStream.of(1, 2, 3, 4, 5).max();
                
                // 기본 타입의 특화된 스트림이 아니더라도 Stream<Integer>와 같은 경우 지원되지만 Compartor를 구현해야 한다.
                OptionalInt min = list.stream().min(Integer::compare);
                OptionalInt max = list.stream().max((a, b) -> Integer.compare(a, b));

                // average
                OptionalDouble average = DoubleStream.of(1.1, 1.2, 1.3, 1.4, 1.5)
                                                    .average();     

                //List에서 집계함수의 응용
                List<Integer> list = List.of(1, 2, 3, 4, 5);

                // sum()
                int sum = list.stream().mapToInt(num -> num.intValue()).sum();
                int sum = list.stream().reduce((a,b) -> a+b).get();

                // average()
                OptionalInteger average = list.stream().mapToInt(num -> num.intValue()).sum();
                OptionalInteger average = list.stream().reduce((a,b) -> a+b).map(n -> n/list.size());
            ~~~
        3. 요소의 검사
            - Predicate<T> 조건식을 인자로 받아서 해당 조건을 만족하는 요소가 있는지 체크하여 처리한다.
            - 결과 값이 boolean 형태임으로 주로 데이터 검사나 검증할때 많이 쓰인다. 또한 중첩 배열 혹은 컬렉션일때도 데이터 검사를 위해 쓰인다.
            1. anyMatch : 조건식이 하나라도 만족하면 true
            2. allMatch : 조건식이 전부 만족해야만 true
            3. noneMatch : 조건식이 모두 만족하지 않아야만 true
            ~~~ java
                // 기본적인 Matching 함수들 사용 예
                List<Integer> list = List.of(1, 3, 5, 7, 9);
                //anyMatch()일 경우
                boolean anyMatch = list.stream().anyMatch(num -> num > 8); // true
                //allMatch()일 경우
                boolean allMatch = list.stream().allMatch(num -> num > 0); // true
                //noneMatch()일 경우
                boolean noneMatch = list.stream().noneMatch(num -> num > 8); // false;

                // 중첩 배열의 경우 아래와 같이 쓰일 수 있다.
                List<Integer> list1 = List.of(1, 3, 5, 7, 9);
                List<Integer> list2 = List.of(2, 4, 6, 8);
                boolean duplex = list1.stream().anllMatch(num -> list2.stream().anyMatch(num2 -> num > num2));
            ~~~
        4. 요소의 검색
            - 스트림에서 지정한 첫번째 요소를 찾는 메서드
            - 보통 filter()와 같이 사용되고 리턴 타입은 Optional<T>이다.
            1. findFist : 스트림에서 지정한 첫번째 요소를 가져온다.
            2. findAny
                - 원소의 정렬과 상관없이 맨 처음 발견된 요소를 찾는다.(병렬스트림의 경우 순서가 보장되지 않기에 사용가능하다)
                - 보통은 findFirst와 결과가 같지만 데이터 요소가 많아지고 필터 조건이 복잡해질 경우 달라질수도 있다.
            ~~~ java
                // 직렬 스트림 findFirst일 경우
                Optional<String> el = List.of("aA", "bB","cC", "ab", "bc", "ac")
                                        .stream()
                                        .filter(s -> s.startWith("a"))
                                        .findFirst();
                // 병렬 스트림 findAny일 경우
                Optional<String> el = List.of("aA", "bB","cC", "ab", "bc", "ac")
                                        .parallelStream()
                                        .filter(s -> s.startWith("a"))
                                        .findAny();
            ~~~
        5. 요소의 소모(결과 합치기)
            - 스트림의 요소를 하나식 줄여가면서 계산하고 최종결과를 반환한다. 
            - 처음 두 요소를 가지고 계산 후 나온 결과로 쭉 다음 요소와 연산하고 결과를 도출한다.
            1. reduce
                - 특징적으로 3가지 형태로 오보로딩된 함수를 제공한다.
                    1. Optional<T> reduce(BinaryOperator<T> accumulator)
                    2. T reduce(T identity, BinaryOperator<T> accumulator)
                    3. U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator\<U\> combiner)
                - 총 3가지의 파라미터를 받을 수 있는데
                    1. accumulator : 각 요소를 처리하는 로직(중간 결과 생성 로직)
                    2. identity : 계산을 위한 초기 값
                    3. combiner : 병렬 스트림에서 사용되며 나눠 계산할 결과를 하나로 합치는 동작하는 로직
                ~~~ java
                    List<Integer> list = List.of(1, 2, 3, 4, 5);

                    // 인자가 하나인 reduce 
                    // Optional<T> reduce(BinaryOperator<T> accumulator)
                    Optional<Integer> reduce = list.stream().reduce((a,b) -> a+b);

                    // accumulator 직접 구현의 경우
                    // BinaryOperator<T>에서 T는 input, output type이다.
                    // 예를 들어 BinaryOperator<Integer>로 선언하게 되면 
                    // 내부 구현체 함수 apply는 input 데이터 2개의 데이터 타입은 Integer가 되고, output인 return 또한 Integer가 된다.
                    BinaryOperator<Integer> accumulator = new BinaryOperator<Integer>() {
			            @Override
			            public Integer apply(Integer arg0, Integer arg1) {
				            return arg0 + arg1;
			            }
		            };
                    Optional<Integer> reduce = list.stream()
                                                   .reduce((a,b) -> accumulator.apply(a, b));

                    // 인자가 두개인 reduce 
                    // T reduce(T identity, BinaryOperator<T> accumulator)
                    // return type이 Optional<T>가 아닌 기본 타입인 이유는 초기값이 설정되어 있기 때문
                    int initalize = 0; // 연산의 초기 값
                    int reduce = list.stream()
                                         .reduce(initalize, (a,b) -> a+b);

                    
                    // 인자가 3개인 reduce
                    // 병렬 스트림에서만 사용이 가능하다.
                    // <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)
                    int initalize = 1; // 연산의 초기 값
                    int reduce = list.stream()
                                         .reduce(initalize, (a,b) -> a+b, (a,b) -> a+b);

                    // BiFunction은 BinaryOperator가 상속받고 있는 부모 인터페이스이다.
                    // BitFunction은 BinaryOperator와 다른점은 input 데이터 2개의 타입와 리턴 타입을 각각 다 제네릭으로 받고 있다.
                    // 즉 앞에서부터 input1 Type, input2 Type, return Type 이다.
                    public interface BinaryOperator<T> extends BiFunction<T,T,T> {
                        ...
                    }

                    // BiFunction을 직접 구현의 경우
                    BiFunction<Integer, Integer, Integer> biFunc = new BiFunction<Integer, Integer, Integer>() {
                        @Override
                        public Integer apply(Integer t, Integer u) {
                            // TODO Auto-generated method stub
                            return t + u;
                        }
                    };

                    int initalize = 1; // 연산의 초기 값
                    int reduce = list.stream()
                                         .reduce(initalize, (a,b) -> biFunc.apply(a, b), (a,b) -> a+b);
                ~~~
        6. 요소의 수집
            1. collect
                - 최종연산 중에서 복잡하면서도 유용하게 활용도가 높으며 보통 수집 전략, 다중 레벨 그룹화 혹은 분할 등 수집 작업의 구성하는 등으로 사용됩니다.
                - 즉, '어떻게 스트림 요소를 수집할 것인가'라고 볼수 있습니다. 이 방법을 정의한 것이 Collector입니다.
                - Collect는 Collector를 인자로 받아 보통 처리를 수행하며 Collectors 클래스를 이용하면 미리 정의 된 함수들을 이용할 수 있으며 또한 직접 구현도 가능합니다.
                - 정리하면
                    - Collect : 스트림의 최종연산자로 매개변수로 Collector를 요구합니다.
                    - Collector : 인터페이스 클래스, 컬렉터는 이 인터페이스를 구현해야 합니다.
                    - Collectors : 컬렉터를 미리 구현한 클래스, static메서드로 미리 구현된 컬렉터를 제공합니다.
            2. Collectors 내부 구현 된 함수
                - 스트림을 컬렉션 배열로 변환
                    ~~~ java
                    // List의 경우
                    Stream<String> stream = Stream.of("a", "b", "c");
                    
                    // List Collector 직접 구현
                    List<String> list = stream.collect(ArrayList::new, List::add, List::addAll);
                    // List 변환 (Collectors 함수 사용)
                    List<String> list = stream.collect(Collectors.toList());
                    // Set 변환
                    Set<String> set = stream.collect(Collectors.toSet());
                    // Map 변환
                    // 주의 할점으로는 Value 값에 null이 들어가지 않도록 해야 합니다.
                    Map<String, String> map = stream.collect(Collectors.toMap(key -> key, value -> value.toUpperCase()));
                    
                    // Map 변환 직접 구현
                    // Collectors.toMap 매개변수 3개일때
                    // 1번째 매개변수 Key 설정, 2번째 매개변수 value 설정, 3번째 매개변수 key 중복시 value 설정
                    Stream<String> streamKey = Stream.of("a", "b", "c", "a");
                    Map<String, String> result = streamKey.collect(Collectors.toMap(key -> key, value -> value, (a, b) -> "키 중복 발생"));
                    // [[a, 'A키 중복 value 변환'], [b, B], [c, C]]

                    // Map 변환 직접 구현2
                    // Collectors.toMap 매개변수 4개일때
                    // 위와 3번째까지는 동일하고 4번째는 새로 담을 객체를 선언해주는 부분이다.
                    Map<String, String> result = streamKey.collect(Collectors.toMap(key -> key, value -> value, (a, b) -> "키 중복 발생", HashMap::new));
                    ~~~
                - 집계 함수
                    ~~~ java
                    Stream<Integer> stream = Stream.of(1, 3, 5, 7, 9);

                    // counting()
                    long count = stream.collect(Collectors.counting());
                    // sum()
                    long sum = stream.collect(Collectors.summingInt(i -> i));
                    // max()
                    Optional<Integer> max = stream.collect(Collectors.maxBy(Comparator.comparingInt(i -> i)));
                    // average()
                    Double average = stream.collect(Collectors.averagingInt(v -> v));
                    // 위 정보들이 모두 필요할때
                    IntSummaryStatistics statistics = stream.collect(Collectors.summarizingInt(Product::getAmount));
                    ~~~
                - 리듀싱
                    - 위에서 설명한 요소의 소모(결과 합치기)와 같은 기능을 하며 3가지 오보로딩 함수가 존재합니다.
                    ~~~ java
                        Stream<Integer> stream = Stream.of(1, 3, 5, 7, 9);  
                        
                        // 일반적인 선언 - 매개변수 1개
                        // Collectors.reducing(BinaryOperator<T> op)
                        Optional<Integer> reduce = stream.collect(Collectors.reducing(Integer::sum));
                        Optional<Integer> reduce = stream.collect(Collectors.reducing((a, b) -> a + b));

                        // 매개변수 2개
                        // Collectors.reducing(T identity, BinaryOperator<T> op)
                        int initialize = 1; // 초기 값
                        int reduce = stream.collect(Collectors.reducing(initialize, Integer::sum));
                        
                        // 매개변수 3개
                        // Collectors.reducing(T identity, Function<T, U> mapper, BinaryOperator<T> op)
                        Stream<Character> stream = Stream.of('a', 'b', 'c');
                        int initialize = 1; // 초기 값
                        int reduce = stream.collect(Collectors.reducing(initialize, ch -> (int) ch, Integer::sum));
                    ~~~
                - 문자열 결합
                    - 문자열 스트림의 모든 요소를 하나의 문자열로 연결하거나 구분자, 접두사, 접미사를 지정하여 연결도 가능합니다.
                    - 단, 스트림 요소들은 CharSequence의 자손 클래스들만 가능합니다. 그래서 만약 자손 클래스가 아니라면 map을 이용하여 변환후 사용해야 합니다.
                    ~~~ java
                        Stream<String> stream = Stream.of("a", "b", "c");

                        // 일반적인 문자열 결합
                        String joining = stream.collect(Collectors.joining());
                        // ["abc"]

                        // 구분자로 결합
                        String joining = stream.collect(Collectors.joining("/"));
                        // ["a/b/c"]

                        // 구분자, 접미사, 접두어로 결합
                        String joining = stream.collect(Collectors.joining("/", "[", "]"));
                        // ["[a/b/c]"]

                        // String이 아닌 객체로 만약 joining()함수를 실행할 경우 toString()을 결합합니다.
                        // 아래 예제는 map()을 통해 Integer -> String 변환후 처리
                        Stream<Integer> stream = Stream.of(1, 2, 3);
                        String joining = stream.map(String::valueOf).collect(Collectors.joining());
                        String joining = stream.map(i -> String.valueOf(i)).collect(Collectors.joining());
                        // ["123"]
                    ~~~
                - 그룹화와 분할
                    - 그룹화는 스트림의 요소를 특정 기준으로 그룹화하는 것을 의미합니다. 
                    - 그룹화할때 조심해야할 점은 직렬방식이 아닌 병렬 방식의 경우에 쓰레드 세이프하지 않다면 데이터가 꼬일수 있는 상황이 발생하는데 이때 Concurrent 클래스 변수를 사용하거나 groupingByConcurrent()를 사용하면 됩니다.
                    - 분할은 스트림의 두가지 요소를 지정된 조건에 일치하는 그룹과 일치하지 않는 그룹으로 분할 한다를 의미합니다.
                    - 분할의 매개변수 함수는 Predicate<T>인데 이 함수는 boolean을 리턴함으로 리턴식은 Map<Boolean, Object>이며 두 그룹으로만 나눌수 있다.
                    ~~~ java
                        // 그룹화
                        Stream<String> stream = Stream.of("ab", "abc", "bc");

                        // 일반적인 그룹화
                        // Collectors.groupingBy(Function func)
                        // 매개변수는 그룹화 조건식을 나타냅니다.
                        Map<Boolean, List<String>> grouping = stream.collect(Collectors.groupingBy(str -> str.length() > 2));
                        // [false=[ab, bc], true=[abc]]
                        Map<Boolean, List<String>> grouping = stream.collect(Collectors.groupingBy(str -> str.charAt(0)));
                        // [a=[ab, abc], b=[bc]]
                        
                        // 매개변수 2개
                        // Collectors.groupingBy(Function func, Collector downStream)
                        // 매개변수는 각, 그룹화 조건식, 결과 처리를 나타냅니다.
                        Map<Boolean, List<String>> grouping = stream.collect(Collectors.groupingBy(str -> str.length() > 2, Collectors.toList()));
                        Map<Boolean, Set<String>> grouping = stream.collect(Collectors.groupingBy(str -> str.length() > 2, Collectors.toSet()));
                        // 이 경우 카운트 결과가 value로 들어간다.
                        Map<Boolean, Long> grouping = stream.collect(Collectors.groupingBy(str -> str.length() > 2, Collectors.counting());
                        // [a=2, b=1]

                        // 매개변수 3개
                        // Collectors.groupingBy(Function func, Supplier mapFactory, Collector downStream)
                        // 매개변수는 각, 그룹화 조건식, 공급자(결과 Type),결과처리를 나타냅니다.
                        Map<Character, Long> grouping = stream.collect(Collectors.groupingBy(ab -> ab.charAt(0), HashMap::new, Collectors.counting()));
                        Map<Character, Long> grouping = stream.collect(Collectors.groupingBy(ab -> ab.charAt(0), () -> new HashMap<>(), Collectors.counting()));

                        // 다수준 그룹화
                        // 길이별로 그룹화 이후 문자열 앞자리로 다시 그룹화
                        Map<Boolean, Map<Object, List<String>>> grouping = stream.collect(Collectors.groupingBy(str -> str.length() > 2, Collectors.groupingBy(str -> str.charAt(0))));
                        // [false=[a=[ab], b=[bc]], true=[a=[abc]]]

                        // 분할
                        Stream<String> stream = Stream.of("ab", "abc", "bc");
                        
                        // 일반적인 분할
                        // Collectors.partitioningBy(Predicate predicate)
                        Map<Boolean, String> partition = stream.collect(Collectors.partitioningBy(str -> str.length() > 2));
                        // [[false, [ab, bc]], [[true, [abc]]]

                        // 매개변수 2개
                        // Collectors.partitioningBy(Predicate predicate, Collector downStream)
                        Map<Boolean, Long> partition = stream.collect(Collectors.partitioningBy(str -> str.length() >2, Collectors.counting()));
                        Map<Boolean, List<String>> partition = stream.collect(Collectors.partitioningBy(str -> str.length() >2, Collectors.toList());
                        
                        // 분할 조건에 해당하는 값 중에서 다시 조건식을 통해서 값을 변경할 수 있다.
                        // 예) 문자열 길이가 2이상으로 분할한 이후 길이가 가장 긴 데이터만 가져온다.
                        Stream<String> stream = Stream.of("a", "ab", "abc", "bcda");
                        Map<Boolean, Optional<String>> partition = stream.collect(
                                                                            Collectors.partitioningBy(str -> str.length() >2, 
                                                                            Collectors.maxBy(Comparator.comparingInt(s -> s.length()))));   
                        // [false=Optional[ab], true=Optional[bcda]]

                        // Collectors.collectingAndThen(Collector collector, Function<R, RR> finisher)
                        // collectingAndThen은 컬렉터 함수 이후 결과처리를 변경하고 싶을때 사용한다.
                        // 예) 위 예제에서 결과 value가 Optional<String>인데 이를 String으로 변환한다.
                        Map<Boolean, String> partition = stream.collect(
                                                                            Collectors.partitioningBy(str -> str.length() >2, 
                                                                                    Collectors.collectingAndThen(
                                                                                        Collectors.maxBy(Comparator.comparingInt(s -> s.length()),
                                                                                        Optinal::get)
                                                                                    )
                                                                                )
                                                                            );   
                        // [false=[ab], true=[bcda]]
                    ~~~
            3. Collector 직접 구현
                - 클래스 상속에 의한 구현 : Collector를 상속받아서 내부 구현 함수들을 구현하는 방식
                ~~~ java
                    // 여기서는 제네릭 타입을 설정하였지만 명시적으로 타입을 지정해도 된다.
                    // 예시) public class collec implements Collector<String, String, String>
                    // 제네릭 타입 T는 input, A는 input 데이터를 중간 계산 이후 타입, R은 output(return)이다
                    // 예를 들면 T=String, A=List<String>, R=List<String>
                    // 1. supplier() 함수를 통해 작업의 결과를 저장할 인스턴스 생성
                    // 2. accumulator()를 통해 Stream의 요소인 T=String을 조건식을 통해 판별 이후 A=List<String>에 저장합니다.
                    // 3. 병렬을 사용할 경우 combiner()를 통해서 병합 합니다. 이때 병합의 결과는 A=List<String>입니다.
                    // 4. 이후 finisher()를 통해서 최종적으로 A=List<String>을 R=List<String>으로 변환하여 결과를 생성합니다.
                    public class collec<T, A, R> implements Collector<T, A, R>{

                        // 작업의 결과를 저장할 공간 제공하는 함수
                        @Override
                        public Supplier<A> supplier() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        // 스트림 요소(input)를 수집할 조건 혹은 방법을 제공하는 함수
                        @Override
                        public BiConsumer<A, T> accumulator() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        // 병렬일때 쓰이며 결과를 어떻게 병합할 것인지 방법을 제공하는 함수
                        @Override
                        public BinaryOperator<A> combiner() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        // 최종 결과를 최종적으로 어떻게 변환할 방법을 제공하는 함수
                        @Override
                        public Function<A, R> finisher() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        // 컬렉터가 수행하는 작업의 속성을 제공하는 함수
                        // Characteristics.CONCURRENT : 병렬로 처리할 수 있는 작업
                        // Characteristics.UNORDERED : 스트림의 요소가 순서가 유지될 필요가 없는 작업
                        // Characteristics.IDENTITY_FINISH : finisher가 항등 함수인 작업
                        @Override
                        public Set<Characteristics> characteristics() {
                            // TODO Auto-generated method stub
                            return null;
                        }
                    }
                ~~~
                - Colletors.of() 구현
                    - 매개변수가 4개인 것과 5개인 것이 존재하는데, 4개인 매개변수에는 finisher() 없는 차이가 잆습니다.
                    - 나머지 매개변수 타입은 위 상속받는 함수와 동일합니다.
                    ~~~ java
                        // 예) 문자열 길이가 2이상인 리스트만 리스트로 뽑아냅니다.
                        Stream<String> stream = Stream.of("a", "ab", "abc", "bcde");

                        public Collector<String, List<String>, List<String>> collector() {
                            return Collector.of(
                                ArrayList::new, // Supplier<A> supplier
                                (list, str) -> { // BiConsumer<A, T> accumulator()
                                    // 길이가 2이상인 리스트만 리스트에 담습니다.
                                    if(str.length() >= 2) {
                                        list.add(str)
                                    }
                                },
                                (list1, list2) -> null, //BinaryOperator<A> combiner(), 병렬 스트림을 사용하지 않기에 구현하지 않아도 됩니다.
                                (R) -> { // Function<A, R> finisher()
                                    return R; // 중간 계산된 타입을 결과 R타입이 같으므로 R을 그냥 리턴
                                },
                                Characteristics.UNORDERED);
                        }
                    ~~~
3. 람다식 
    1. Supplier<T>
        - 매개변수는 없고, 반환값만 존재하는 함수형 인터페이스
            ~~~ java
            // Supplier 인터페이스 클래스
            @FunctionalInterface
            public interface Supplier<T> {
                T get();
            }

            Supplier<String> supplier = () -> "";
            supplier.get();

            // 아래와 같은 형식으로 만들 수 있는데 그러면 함수형 인터페이스를 사용하는 의미가 없으므로 예시로 넣지 않겠습니다.
            Supplier<T> supplier = new Supplier<T>() {
                @Override
                public T get() {
                    return null;
                }
            }
            ~~~
    2. Consumer<T> 
        - Supplier와는 반대로 매개변수만 존재하고 반환값이 존재하지 않는 함수형 인터페이스
            ~~~ java
            // Consumer 인터페이스 클래스
            @FunctionalInterface
            public interface Consumer<T> {
            void accept(T t);
            }

            Consumer<String> consumer = (str) -> System.out.println(str);
            consumer.accept("test");
            ~~~
    3. Function<T,R>
        - 일반적인 함수, 하나의 매개변수를 받아서 결과를 처리하는 함수형 인터페이스
            ~~~ java
            // Function 인터페이스 클래스
            @FunctionalInterface
            public interface Function<T, R> {
                R apply(T t);
            }

            Function<Integer, String> function = (num) -> String::valueOf; // T타입을 R타입으로 변환해야 합니다.
            String result = function(1);
            ~~~
    4. Predicate<T>
        - 조건식을 표현하는데 사용되는 함수형 인터페이스
        - Function의 변형으로 반환값이 boolean이란 것만 다릅니다.
            ~~~ java
            // Predicate 인터페이스 클래스
            @FunctionalInterface
            public interface Predicate<T> {
                boolean test(T t);
            }

            Predicate<String> predicate = (str) -> str.length > 2;
            boolean result = predicate.test("abc");
            ~~~
    5. BiConsumer<T,U>
        - 두개의 매개변수만 있고 반환 값이 없다.
            ~~~ java
            // BiConsumer 인터페이스 클래스
            @FunctionalInterface
            public interface BiConsumer<T, U> {
            void accept(T t, U u);
            }

            BiConsumer<Integer, String> biConsumer = (num, str) -> System.out.println(str + " : " + num);
            biConsumer.accpet("test", 1);
            ~~~
    6. BiPredicate<T,U>
        - 조건식을 표현하는데 사용되는데 매개변수가 두개이다.
            ~~~ java
            // BiPredicate 인터페이스 클래스
            @FunctionalInterface
            public interface BiPredicate<T, U> {
                boolean test(T t, U u);
            }

            BiPredicate<Integer, Integer> biPredicate = (num, str) -> str.length() == num;
            biPredicate.test(1, "ab");
            ~~~
    7. BiFunction<T,U,R>
        - 두 개의 매개변수를 받아서 하나의 결과를 반환 합니다.
            ~~~ java
            // BiFunction 인터페이스 클래스
            @FunctionalInterface
            public interface BiFunction<T, U, R> {
                R apply(T t, U u);
            }

            BiFunction<Integer, Integer, Integer> biFunction = (i1, i2) -> i1 * i2;
            Integer result = biFunction.apply(1, 2);
            ~~~
    8. UnaryOperator<T>
        - Function의 자손 클래스로 매개변수와 결과타입이 같다.
            ~~~ java
            // UnaryOperator 인터페이스 클래스
            @FunctionalInterface
            public interface UnaryOperator<T> extends Function<T, T> {
            }

            UnaryOperator<Integer> unaryOperator = num -> num * num;
            Integer result = unaryOperator.apply(2);
            ~~~ 
    9. BinaryOperator<T>
        - BiFunction의 자손 클래스로 매개변수와 곁과 타입이 같다.(매개변수가 2개이며 결과는 하나만 반환)
            ~~~ java
            // BinaryOpertor 인터페이스 클래스
            @FunctionalInterface
            public interface BinaryOperator<T> extends BiFunction<T,T,T> {
            }

            BinaryOperator<Integer> binaryOperator = (i1, i2) -> i1 * i2;
            Integer result = binaryOperator.appy(1, 2);
            ~~~
4. 참고 사이트
    - https://rebeccacho.gitbooks.io/java-study-group/content/chapter14.html
    - http://cr.openjdk.java.net/~briangoetz/lambda/lambda-state-final.html
    - https://codechacha.com/ko/java8-stream-collect/
    - 자바의 정석