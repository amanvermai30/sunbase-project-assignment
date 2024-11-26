document.querySelector("form").addEventListener("submit",userLogin)

 function userLogin(event){

    event.preventDefault();
    console.log("working")

    let email = document.getElementById("email").value
    let password = document.getElementById("password").value

    let obj={};
    let type = "CANLOGIN";

    obj["email"]= email
    obj["password"]=password

    console.log(obj);
    userLoginFun(obj)

}

async function userLoginFun(obj){
    try {
        let res = await fetch(`http://localhost:8080/auth/login`, {
            method: "POST",
            body:JSON.stringify(obj),
            headers: {
                "Content-Type": "application/json"
            }
            // body:JSON.stringify(obj)
        })
        console.log(res)
        if (res.ok) {
            console.log("sucesss")
            let data = await res.json();

            localStorage.setItem("token", data?.payload);
            console.log(data?.payload);
            console.log(data);

            alert("customer login successfuly")
            window.location.href = "./dashboard.html";
        }
    } catch (error) {
        console.log(error)
        alert("failed to login")
    }
}