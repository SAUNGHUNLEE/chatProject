document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');

    loginForm.addEventListener('submit',function(e){
        e.preventDefault();
        email = emailInput.value;
        password = passwordInput.value;

        fetch("/unauth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email: email, password: password }),
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/unauth/signup"; // 로그인 성공 시 이동할 페이지
                } else {
                    document.getElementById("errorMessage").style.display = "로그인실패"; // 에러 메시지 표시
                }
            })
            .catch(error => console.error("Error:", error));
    });
});

