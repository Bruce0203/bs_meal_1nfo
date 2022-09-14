# 인스타그램에 학교 급식 자동 공지 봇

# 요구 사항
- GitHub Actions Runner (작동할 개인 컴퓨터)
- Instargram Creator/Business account (인스타그램 계정)
- 2FA OTP Secrets Key (2단계 인증 키)

# 포함된 기능
1. 급식 정보 얻기
2. 글씨써서 이미지로 만들기
3. 인스타그램 포스팅
4. GH Actions 자동화

---

Q: 어떻게 자동으로 공지해주나요?  

A: 대략 6시간 마다 자동으로 프로그램을 실행시켜서 오늘의 급식이 있고, 마지막 게시물의 날짜가 오늘과 다를 때, 게시합니다. 

---

Q: 안해도 될텐데 왜 굳이 GitHub Actions Runner 가 필요하나요?  


A: 인스타그램 API가 여러 디바이스에서 로그인하는 것을 허용하지 않아서, 매번 같은 디바이스에서 작동해야 합니다.

--- 
Q: 배경 이미지, 글자 조정을 하기 위한 설정이 있나요?  


A: `assets` 폴더에서 폰트, 이미지, 글씨 스타일을 설정할 수 있습니다! 

---

# 설치

- 이 repo를 fork 하세요
- fork한 GitHub Actions를 활성화하고 아무거나 push하세요
- 인스타그램 크리에이터, 혹은 비즈니스 계정을 만드세요
- `env.example` 파일을 참고해서, GitHub에서 Settings의 Security 카테고리의 Secrets > Actions 를 설정하세요
- 마지막으로, GitHub Actions Runner 프로그램을 당신의 컴퓨터에 24시간 작동시켜두세요!