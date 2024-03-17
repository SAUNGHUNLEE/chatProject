document.addEventListener("DOMContentLoaded", function () {
    let conditions = {
        emailValid: false,
        nameValid: false,
        passwordValid: false,
        emailVerified: false,
        //birthdayValid: false // 생년월일 유효성 추가
    };

    const emailInput = document.querySelector('input[name="email"]');
    const nameInput = document.querySelector('input[name="name"]');
    const form = document.querySelector('form');

    const birthdayInput = document.querySelector('input[name="birthDay"]');
    const passwordInput = document.querySelector('input[name="password"]');
    const submitButton = document.querySelector('button[type="submit"]');
    const emailFeedback = document.getElementById('emailFeedback');
    const nameFeedback = document.getElementById('nameFeedback');
    const passwordFeedback = document.getElementById('passwordFeedback');
    const verifyEmailButton = document.getElementById('verifyEmail');

    // CSRF 토큰과 헤더 이름을 가져오는 코드
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]').content;

    const emailTokenInput = document.getElementById('emailToken');
    const verifyTokenButton = document.getElementById('verifyToken');
    const verificationResultSpan = document.getElementById('verificationResult');

    verifyTokenButton.addEventListener('click', function() {
        const emailToken = emailTokenInput.value;

        fetch(`/unauth/user/confirm-email?token=${emailToken}`)
            .then(response => {
                if(response.ok) {
                    return response.json();
                } else {
                    throw new Error('Verification failed');
                }
            })
            .then(data => {
                verificationResultSpan.textContent = '인증성공';
                updateSubmitButtonState()
                // 추가적인 성공 처리 로직
            })
            .catch(error => {
                console.error('Error:', error);
                verificationResultSpan.textContent = '인증실패';
                updateSubmitButtonState()
            });
    });


    verifyEmailButton.addEventListener('click', function() {
        const email = emailInput.value;
        if(email) {
            fetch('/unauth/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email: email })
            })
                .then(response => response.json())
                .then(data => {
                    emailVerificationFeedback.textContent = '인증 메일이 전송되었습니다. 메일을 확인해주세요.';
                })
                .catch(error => {
                    console.error('Error:', error);
                    emailVerificationFeedback.textContent = '메일 전송 실패. 다시 시도해주세요.';
                });
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


                    } else {
                        document.getElementById('emailFeedback').textContent = "";

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

                    } else {
                        document.getElementById('nameFeedback').textContent = "";

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
    const updateSubmitButtonState = () => {
        submitButton.disabled = !Object.values(conditions).every(value => value);
    };

    emailInput.addEventListener("input", () => {
        conditions.emailValid = emailInput.value.length > 0;
        updateSubmitButtonState();
    });

    nameInput.addEventListener("input", () => {
        conditions.nameValid = nameInput.value.length > 0;
        updateSubmitButtonState();
    });

    passwordInput.addEventListener("input", () => {
        conditions.passwordValid = passwordInput.value.length >= 8;
        updateSubmitButtonState();
    });
/*    birthdayInput.addEventListener("input", function () {
        conditions.birthdayValid = birthdayInput.value !== '';
        updateSubmitButtonState();
    });*/

    updateSubmitButtonState()
});

