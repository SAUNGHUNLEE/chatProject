document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.querySelector('input[name="email"]');
    const nameInput = document.querySelector('input[name="name"]');
    const form = document.querySelector('form');

    const passwordInput = document.querySelector('input[name="password"]');
    const submitButton = document.querySelector('button[type="submit"]');
    const emailFeedback = document.getElementById('emailFeedback');
    const nameFeedback = document.getElementById('nameFeedback');
    const passwordFeedback = document.getElementById('passwordFeedback');
    const verifyEmail = document.getElementById('verifyEmail');

    // CSRF 토큰과 헤더 이름을 가져오는 코드
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]').content;



    // 이메일 인증 버튼 클릭 이벤트 리스너
    verifyEmail.addEventListener('click', function() {
        const email = emailInput.value;
        if(email) {
            // CSRF 토큰과 함께 POST 요청
            fetch(`/unauth/send-verification-email`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    [csrfHeaderName]: csrfToken // CSRF 토큰 추가
                },
                body: JSON.stringify({ email: email }) // 요청 본문에 이메일 주소를 JSON 형태로 전송
            })
                .then(response => {
                    if(response.ok) {
                        emailVerificationFeedback.textContent = '인증 메일이 전송되었습니다. 메일을 확인해주세요.';
                    } else {
                        emailVerificationFeedback.textContent = '메일 전송 실패. 다시 시도해주세요.';
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    });


    emailInput.addEventListener("blur", function () {
        const email = emailInput.value;
        if (email) {
            fetch(`/unauth/check-email?email=${email}`)
                .then(response => response.json())
                .then(data => {
                    if (data.isEmailTaken) {
                        document.getElementById('emailFeedback').textContent = "이미 사용 중인 이메일 입니다.";
                        form.querySelector('button[type="submit"]').disabled = false;
                    } else {
                        document.getElementById('emailFeedback').textContent = "";
                        form.querySelector('button[type="submit"]').disabled = false;
                    }
                }).catch(console.error)
        }
    });

    nameInput.addEventListener("blur", function () {
        const name = nameInput.value;
        if (name) {
            fetch(`/unauth/check-name?name=${name}`)
                .then(response => response.json())
                .then(data => {
                    if (data.isNameTaken) {
                        document.getElementById('nameFeedback').textContent = "이미 사용 중인 이름 입니다."
                        form.querySelector('button[type="submit"]').disabled = false;
                    } else {
                        document.getElementById('nameFeedback').textContent = "";
                        form.querySelector('button[type="submit"]').disabled = false;
                    }
                }).catch(console.error)
        }
    });

    /*  passwordInput.addEventListener("input", function () {
          // 비밀번호 조건 검사
          const password = passwordInput.value;
          let isValid = true;
          const minLength = 8;
          const hasUpperCase = /[A-Z]/.test(password);
          const hasNumber = /[0-9]/.test(password);

          if (password.length < minLength) {
              message = "비밀번호는 8자 이상이어야 합니다.";
          }
          if (!hasUpperCase) {
              message += " 대문자를 포함해야 합니다.";
          }
          if (!hasNumber) {
              message += " 숫자를 포함해야 합니다.";
          }

          passwordFeedback.textContent = message;
          submitButton.disabled = message !== "";
      });*/
});

