document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('pwEmail');
    const passwordInput = document.getElementById('password');
    const emailVerificationFeedback = document.getElementById('emailVerificationFeedback');
    const nameInput = document.getElementById('pwName');
    const phoneInput = document.getElementById('pwPhone');

    const findEmailButton = document.getElementById('findEmailButton');
    const submitFindEmail = document.getElementById('submitFindEmail');

    const findPwButton = document.getElementById('findPasswordButton');
    const submitFindPw = document.getElementById('submitFindPw');

    const emailTokenInput = document.getElementById('emailToken');
    const verifyTokenButton = document.getElementById('verifyToken');
    const verificationResultSpan = document.getElementById('verificationResult');
    const verifyEmailButton = document.getElementById('verifyPwEmail');
    // 비밀번호 변경 모달 내의 폼과 버튼 참조
    const changePasswordForm = document.getElementById('changePasswordForm');
    const submitChangePasswordButton = document.getElementById('submitChangePassword');


    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();
        email = emailInput.value;
        password = passwordInput.value;

        fetch("/unauth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({email: email, password: password}),
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


    findEmailButton.addEventListener('click', function () {
        $('#findEmailModal').modal('show');//모달 창 표시
    });

    submitFindEmail.addEventListener('click', function () {
        const name = document.getElementById('name').value;
        const phone = document.getElementById('phone').value;


        fetch('/unauth/find-email', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",

            },
            body: JSON.stringify({name: name, phone: phone})
        })
            .then(response => response.json())
            .then(data => {
                if (data.email) {
                    alert('당신의 이메일은' + data.email + '입니다.');
                    $('#findEmailModal').modal('hide');
                } else {
                    alert('해당 정보와 일치하는 이메일이 없습니다.');
                }
            })
            .catch(error => console.error('Error:', error));
    });


    //비밀번호 찾기 위한 이메일 인증
    findPwButton.addEventListener('click', function () {
        $('#findPasswordModal').modal('show');
    });

    verifyEmailButton.addEventListener('click', function () {
        const email = emailInput.value;
        //이름 + 핸드폰 번호 = 이메일 여부 확인
        const name = nameInput.value;
        const phone = phoneInput.value;

        fetch('/unauth/check-user-email', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: name,
                phone: phone,
                email: email
            })
        })
            .then(response => {
                if (response.ok) {
                    // User details matched; proceed to send verification email
                    return fetch('/unauth/check-user-email', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({email: email})
                    });
                } else {
                    throw new Error('유저정보와 계정이 존재하지 않습니다.');
                }
            })
            .then(response => response.json())
            .then(data => {
                emailVerificationFeedback.textContent = '인증 메일이 전송되었습니다. 메일을 확인해주세요.';
            })
            .catch(error => {
                console.error('Error:', error);
                emailVerificationFeedback.textContent = '계정 정보 검증에 실패하였거나 메일 전송에 실패하였습니다. 다시 시도해주세요.';
            });
    });


    verifyTokenButton.addEventListener('click', function () {
        const emailToken = emailTokenInput.value;

        fetch(`/unauth/user/confirm-email?token=${emailToken}`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('인증실패');
                }
            })
            .then(data => {
                verificationResultSpan.textContent = '인증성공';
                // 인증 성공 처리 로직 후
                //$('#findPasswordModal').modal('hide'); // 기존 모달 숨기기
                $('#changePasswordModal').modal('show'); // 비밀번호 변경 모달 표시


            })
            .catch(error => {
                console.error('Error:', error);
                verificationResultSpan.textContent = '인증실패';
            });
    });


    // "변경하기" 버튼 클릭 이벤트 리스너 등록
    submitChangePasswordButton.addEventListener('click', function(event) {
        event.preventDefault(); // 폼 기본 제출 동작 방지

        // 새 비밀번호 및 새 비밀번호 확인 입력값 가져오기
        const newPassword = document.getElementById('newPassword').value;
        const confirmNewPassword = document.getElementById('confirmNewPassword').value;
        const token = emailTokenInput.value;

        // 새 비밀번호와 새 비밀번호 확인이 일치하는지 검증
        if(newPassword !== confirmNewPassword) {
            alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
            return;
        }

        // FormData 객체를 사용하여 폼 데이터 준비
        const formData = new FormData(changePasswordForm);
        formData.append('newPassword', newPassword);
        formData.append('confirmNewPassword', confirmNewPassword);

        // Fetch API를 사용하여 비밀번호 변경 요청 전송
        fetch('/unauth/change-pw', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({token: token, newPassword: newPassword})
        })
            .then(response => {
                if (response.ok) {
                    alert('비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요.');
                    window.location.href = '/unauth/login'; // 성공 시 로그인 페이지로 리다이렉션
                } else {
                    throw new Error('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
                }
            })
            .catch(error => console.error('Error:', error));

    });


});