# liefeni

## 문의 주소

- Discord: [https://discord.gg/hTWdyPGu99](https://discord.gg/hTWdyPGu99)

## 실행 방법

- 위의 Discord 서버에서 배포 채널에 올라온 압축파일을 다운로드하여 실행

## 사용법

hafen client에 builtin된 console을 사용하여 명령을 입력할 수 있습니다
- `:<명령어> help`: \<명령어\> 명령의 man page를 System 창에 출력합니다
- 사용 가능한 명령어 목록
    - lmi
    - man
- 예시
```
:lmi AlignLog
```
> 통나무를 정리하는 AlignLog 자동화 프로그램을 실행합니다
- 자동화 프로그램이 실행중일 때 ESC를 누르면 실행중인 자동화 프로그램을 종료합니다

## 자동화 프로그램 목록

- AlignLog: 널브러진 통나무들을 지정한 공간에 차곡차곡 정리합니다

## 목적

- 원본 코드에 최소한으로 개입하면서 automation 기능을 탑재합니다
    1. 원본 코드의 수정은 가능하면 새로운 라인을 추가하는 것으로 제한합니다
- 최적화를 위해 일부 원본 기능을 제거할 수 있습니다
- 시각적 기능을 추가할 예정은 없습니다
- gui와 상호작용하는 기능을 추가할 예정은 없습니다

## Naming Convention

- \_\<identifier\>: private access modifier를 가진 identifier에 붙입니다
- private method가 아닌 method를 호출 시 invoker를 생략하지 않습니다(ex> this.method(), ClassName.method())

## 앞으로 개발 방향

- 아이템과 상호작용하는 api 추가
