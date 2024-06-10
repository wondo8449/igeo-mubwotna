## 🍚 이거 무 봤나

프로그램 역할 : 요리 초보들을 대상으로 본인만의 레시피를 소개하고 공유하는 사이트
* 프로젝트 기간 : 2024.06.04 ~ 2024.06.11 (8일)
*  🛠️ Tech Stack : <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
* 버전: JDK 17
* 개발 환경: IntelliJ

<br>



## 👩‍💻👨‍💻 팀원 구성
|                                                                                  이창형                                                                                   |                                             김예찬                                              |                                             이은샘                                              |                                             박시현                                              |                                             문수정                                              |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------:|
| <img src="https://velog.velcdn.com/images/wondo8449/post/d8b5de97-4de1-4496-9dfc-526405299630/image.jpg" height="150"/> | <img src="https://velog.velcdn.com/images/wondo8449/post/b4fd99e9-bd63-4607-af73-a07579bb6bdb/image.jpg" height="150"/> | <img src="https://velog.velcdn.com/images/wondo8449/post/a238b821-be20-4eb0-981b-3d560412db4c/image.jpg" height="150"/> | <img src="https://velog.velcdn.com/images/wondo8449/post/1a190f2d-4e25-4d53-89d1-b8888bd35504/image.jpg" height="150"/> | <img src="https://velog.velcdn.com/images/wondo8449/post/a524121d-87fc-48e0-8d3d-c2e469265264/image.jpg" height="150"/> |
|                                                          [@LeeChangHyeong](https://github.com/LeeChangHyeong)                                                          |                             [@wondo8449](https://github.com/wondo8449)                             |                          [@eunsaemsaem](https://github.com/eunsaemsaem)                          |                         [@sihyun615](https://github.com/sihyun615)                         |                              [@sujeongmoon](https://github.com/sujeongmoon)                              |
<br>

## 프로젝트 소개
<details>
<summary> 📑 요구사항 정의 </summary>

* Users : Id, userId, password, name, email, description, status, refresh_token, status_modified_at, create_at, modified_at

* Recipe : id, user_id, content, like, created_at, modified_at
* Comment : id, recipe_id, user_id, content, like, created_at, modified_at
* RecipeLikes : id, user_id, recipe_id, created_at, modified_at
* CommentLikes : id, user_id, comment_id, created_at, modified_at

<br>

* 공통 조건


    * 전체 공통 조건
      
      예외처리는 HttpStatusCode - Message 형태로 처리하여 response
      
      모든 엔티티에는 생성일자와 수정일자가 존재


      
    * 사용자 관련 공통 조건
      
      Spring Security와 JWT를 사용하여 설계 및 구현
      
      JWT는 Access Token, Refresh Token을 구현
      
      Access Token 만료 시 : 유효한 Refresh Token을 통해 새로운 Access Token과 Refresh Token을 발급
      
      Refresh Token 만료 시 : 재로그인을 통해 새로운 Access Token과 Refresh Token을 발급
      
      API를 요청할 때는 Access Token을 사용

</details>
<details>
<summary> ⚙ 기능 명세서 </summary>

<br>

*✔ 필수 기능 / ➕ 추가 기능*
<br>
* 사용자 인증 기능

  ✔ 회원가입

  ✔ 로그인

  ✔ 회원탈퇴

  ✔ 로그아웃



* 프로필 관리 기능

  ✔ 프로필 조회
  
  ✔ 프로필 수정



* 뉴스피드 게시물 CRUD 기능

  ✔ 게시물 작성
  
  ✔ 게시물 조회
  
  ✔ 게시물 수정
  
  ✔ 게시물 삭제
  
  ➕ 페이지네이션
  
  ➕ 생성일자 기준 최신순 조회
  
  ➕ 좋아요 많은 순 조회
  
  ➕ 기간별 검색 기능



* 댓글 CRUD 기능

  ➕ 댓글 작성
  
  ➕ 댓글 조회
  
  ➕ 댓글 수정
  
  ➕ 댓글 삭제



* 좋아요 기능

  ➕ 게시글 좋아요 추가
  
  ➕ 게시글 좋아요 삭제
  
  ➕ 댓글 좋아요 추가
  
  ➕ 댓글 좋아요 삭제


</details>
<br>

### ✍🏻 API 명세
> <img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/76b61b6c-a79e-441b-82d9-634636fcbbff/Untitled.png?id=724fb984-3dfd-4ffe-9be3-f53df190c8c9&table=block&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1718092800000&signature=HqOdwUut2tY3d4ozBFG3dVmLp3CLsnBRW9ZTFXL7OOA&downloadName=Untitled.png" width="750px">
> <img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/9826c9d3-5314-4c4e-9f6b-d5d1664802e0/Untitled.png?id=afeb9fb9-6e21-4ce8-bc00-eb30739e07d9&table=block&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1718092800000&signature=SXNrKG8as8Rphy5kplBghHSz_lESY1OuZvQpMwTb-gI&downloadName=Untitled.png" width="750px">
<br>

### 🧬 ERD
> <img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F33468dca-1e69-4254-8a5c-0c2bccda0b17%2F%25ED%2599%2594%25EB%25A9%25B4_%25EC%25BA%25A1%25EC%25B2%2598_2024-06-05_115803.jpg?table=block&id=4ba44510-60fd-4ca6-91df-f56fe5596e04&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2" width="500px">
<br>


## 🐱 Github Rules
> <img src="https://velog.velcdn.com/images/wondo8449/post/4f4d9075-adf5-41d3-8e0b-48bfac89310b/image.jpg" width="500px">
> <img src="https://velog.velcdn.com/images/wondo8449/post/8cf3bdf8-c5ab-4499-a3a0-9c6b561bce7c/image.jpg" width="500px">

### Issue 예시
> <img src="https://velog.velcdn.com/images/wondo8449/post/891e42c1-f291-4611-9aec-1c18067740d9/image.png" width="500px">
> <img src="https://velog.velcdn.com/images/wondo8449/post/373984d0-f3b7-40c6-a1fe-62ce45fad0d1/image.png" width="500px">

### PR 예시
> <img src="https://velog.velcdn.com/images/wondo8449/post/5b0a68a1-58f3-4dcf-9013-2ff0d5407eab/image.png" width="500px">
> <img src="https://velog.velcdn.com/images/wondo8449/post/a3f96108-e9dc-4497-be32-08dc6d3a86c2/image.png" width="500px">

## 😫 트러블 슈팅
> <img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/312c2eb2-7b77-42f5-8668-ea2c0fa471d4/Untitled.png?id=c231be88-dd98-4376-b22e-f55097f5b8ab&table=block&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1718114400000&signature=Hn_dCFF0Tb9SLUK_jWfg_3vgSRiZypzITUW8UQ3g_8I&downloadName=Untitled.png" width="500px">
> <img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/35c5c602-e1cb-4594-8415-169dc18b587a/Untitled.png?id=a22aa717-75f2-4052-8caa-73cea4d2efeb&table=block&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1718114400000&signature=qht7j9KFCOiZTuv3C8TVy85xNSczF83UzqewuWsajmA&downloadName=Untitled.png" width="500px">

## 🙌 프로젝트 후기
#### 🤡 이창형

#### 🔨 김예찬

#### 🌳 이은샘

#### 👾 박시현

#### 🍔 문수정
