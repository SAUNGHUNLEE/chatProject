document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.querySelector('input[name="email"]');
    const passwordInput = document.querySelector('input[name="password"]');
    const submitButton = document.querySelector('button[type="submit"]');
    const emailFeedback = document.getElementById('emailFeedback');
    const passwordFeedback = document.getElementById('passwordFeedback');

/*    emailInput.addEventListener("blur", function () {
        // 이메일 중복 검사 AJAX 요청
        const email = emailInput.value;
        fetch('/check-email?email=' + email)
            .then(response => response.json())
            .then(data => {
                if (data.isEmailTaken) {
                    emailFeedback.textContent = "이미 사용중인 이메일 입니다.";
                    submitButton.disabled = true;
                } else {
                    emailFeedback.textContent = "";
                    submitButton.disabled = false;
                }
            });
    });*/

    passwordInput.addEventListener("input", function () {
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
    });
});

