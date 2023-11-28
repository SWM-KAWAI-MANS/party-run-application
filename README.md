![image](https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/3234f4ba-24f1-4ccf-8612-9ff17eb4a919)
![image](https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/4a85d098-bf33-4cfd-b7ca-b5c5d62099b7)
![image](https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/129c3f0f-4fa0-49db-bdd0-25337c371cee)

<p align="center">
  <a><img alt="Studio" src="https://img.shields.io/badge/Studio-Giraffe 2022.3.1-orange?style=flat"/></a>
  <a><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-1.8.20-orange?style=flat"/></a>
  <a><img alt="Java version" src="https://img.shields.io/badge/Java version-17-orange?style=flat"/></a>
  <a><img alt="minSdk" src="https://img.shields.io/badge/minSdkVersion-26-orange?style=flat"/></a>
  <a><img alt="targetSdk" src="https://img.shields.io/badge/targetSdkVersion-33-orange?style=flat"/></a>
</p>

---

## 🚦 프로젝트 개요
본 파티런 프로젝트는 단순히 혼자 뛰는 러닝에서 벗어나, 다른 사용자와 함께 하는 Runner Experience(RX)를 제공합니다.
대부분 혼자 이루어지는 달리기를 사회적 액티비티로 전환하고 누군가와 함께 뛰면서 즐길 수 있게 하고, 달리기를 소셜과 연결하여 더 큰 즐거움과 동기부여를 제공합니다.
사용자들은 다른 사용자와 함께 할 수 있는 다양한 기능들을 통해 동기부여를 받으며 더 재미있게 러닝을 즐길 수 있고,
실시간으로 새로운 사람들과 경쟁이나 협동하며 달리며 러너에게 특화된 RX를 느끼게 합니다.

---
## 📹 Preview

| 영상 | 설명 |
| --- | ---- |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/2f19e0fc-84e8-4ebc-8c83-06f2a0c9f520.gif" alt="drawing" width="250px" /> | 로그인 |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/3315743f-f566-44f0-bad1-e91b514dd77d.gif" alt="drawing" width="250px" /> | '배틀모드'의 랜덤매칭부터 카운트다운까지 |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/6a5d49f2-816f-45c5-9905-553a0bfff7e1.gif" alt="drawing" width="250px" /> | 매칭 취소 경우의 수 |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/c793a4b7-591a-4f79-9d37-aeade8bb069c.gif" alt="drawing" width="250px" /> | 싱글모드 |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/a0a4b4b4-eafb-4eab-87a2-b1af006f60dc.gif" alt="drawing" width="250px" /> | 같이하기 |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/0904d72c-568c-4c48-97c7-b5b25c456706.gif" alt="drawing" width="250px" /> | 마이페이지 (종합기록, 싱글히스토리, 대결히스토리) |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/97358028-52a2-42a7-80c9-1baf7c0003fc.gif" alt="drawing" width="250px" /> | 프로필 수정 (사진, 닉네임) |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/251fe447-d331-44c2-89e1-dc1c768b2e9f.gif" alt="drawing" width="250px" /> | 로그아웃 후 다른 계정 로그인 |
| <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/d72b2b86-41d4-4b0f-ac7d-2e4fedb2581c.gif" alt="drawing" width="250px" /> | 회원탈퇴 |

## 전체적인 시연영상
![ezgif com-video-to-gif (1)](https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/03851a2e-bca6-41c0-8757-25ca981b9763)

싱글모드 ~ 배틀모드

---

## 🏢 Architecture Overview

![image](https://github.com/nohjunh/assignment/assets/75293768/05cc3011-0109-45ac-bf33-3c553159b957)

* 본 프로젝트는 데이터, 도메인, UI 레이어를 갖는 단방향 데이터 흐름의 반응형 프로그래밍 모델을 따른다. 
UI 레이어와 같은 상위 레이어가 하위 레이어의 데이터를 제공받기 위해 이벤트 호출하고 그 데이터는 하위 레이어에서 상위 레이어로 흐르는 구조로 진행된다.

<img width="461" alt="스크린샷 2023-10-23 오후 9 30 12" src="https://github.com/nohjunh/assignment/assets/75293768/0d72491c-9ab9-4873-a654-7c56360b9fcd">

* UI 레이어는 JetPack Compose를 사용하여 빌드하였고, ViewModel에서 UseCase, Repository를 통한 데이터 스트림을 수신하고 이를 UI State로 변환한다.
* 사용자가 앱과의 상호 작용하는 방식은 ViewModel에서 이벤트로 처리

</br>

## 🛠️ 예: **결과 화면에 대결(Battle) 결과 표시**

- 대결이 끝나고 나면 data layer에서 서버로부터 대결 결과의 분석정보를 가져오기 위한 이벤트를 호출한다.
- 안드로이드 로컬에 저장되어 있는 대결에 참여했던 러너들의 데이터와 서버로부터 얻은 대결 분석 리소스들을 Domain layer에서 결합해 각 러너들에 맞는 결과 정보를 UI layer 화면에 표시하게 된다.


![image](https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/bcb9cec1-1d41-43b4-89f9-d9e9b0907dce)

---

## 🛠️ 러너 랜덤 매칭을 위한 SSE와 REST 혼용 방식의 설계 및 구현 

![image](https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/122bbd5a-7e5f-4f12-a5c3-c88797bba0e6)

- 매칭에서는 REST API 와 Server Sent Events (SSE)를 사용한다.
- 대결 모드에 참여하려는 러너들로부터 매칭 요청을 받은 후 SSE를 통해 매칭 이벤트를 클라이언트에게 단방향으로 전달
- 전달받은 이벤트를 처리하기 위해 클라이언트는 위 그림처럼 REST API 와 Server Sent Events 네트워크 통신을 단계 별로 적절히 사용하여 매칭과정이 이루어질 수 있도록 구성
- 목표한 러너 수만큼 대기, 다른 러너들의 매칭 여부 대기 경우에 Server Sent Events 를, 그 외
클라이언트가 서버로부터 전달할 데이터가 있는 경우는 REST API 사용

---


## 🏘 Modularization
멀티모듈화 전략을 채택함으로써 모듈 단위로 기능을 분리하여 코드 간의 의존성을 낮추고, 재사용성을 높이며, 빌드 시간을 줄이는 이점을 얻는다.

<table>
    <tr>
        <td>
            <img src="https://github.com/SWM-KAWAI-MANS/party-run-application/assets/75293768/2d863ea8-9331-48ca-b01c-d30e02d74b87.png" alt="Image 1" width="100%">
        </td>
    </tr>
</table>


- **App Module**
   
    앱 모듈은 모든 Feature 모듈과 필요한 핵심 모듈에 의존
    
- **Feature Module**

    앱에서 단일 책임을 처리할 수 있도록 범위가 지정된 기능별 모듈
    
- **Core Module**

   앱의 다른 모듈 간에 공유해야 하는 코드와 특정 종속성을 포함하는 일반 라이브러리 모듈

