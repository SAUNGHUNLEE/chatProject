document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');


    const findEmailButton = document.getElementById('findEmailButton');
    const submitFindEmail = document.getElementById('submitFindEmail');



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


    findEmailButton.addEventListener('click',function(){
        $('#findEmailModal').modal('show');//모달 창 표시
    });

    submitFindEmail.addEventListener('click',function(){
        const name = document.getElementById('name').value;
        const phone = document.getElementById('phone').value;


        fetch('/unauth/find-email',{
            method:'POST',
            headers:{
                "Content-Type":"application/json",

            },
            body:JSON.stringify({name:name,phone:phone})
        })
            .then(response=>response.json())
            .then(data =>{
                if(data.email){
                    alert('당신의 이메일은'+data.email+'입니다.');
                    $('#findEmailModal').modal('hide');
                }else{
                    alert('해당 정보와 일치하는 이메일이 없습니다.');
                }
            })
            .catch(error=>console.error('Error:',error));
    });


});

