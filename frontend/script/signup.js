document.querySelector("form").addEventListener("submit",userSignup)

 function userSignup(event){

    event.preventDefault();
    console.log("working")

    let firstName =document.getElementById("firstName").value
    let lastName = document.getElementById("lastName").value
    let email = document.getElementById("email").value
    let password = document.getElementById("password").value

    let obj={};
    let type = "CANLOGIN";

    obj["firstName"] = firstName
    obj["lastName"]=lastName
    obj["email"]= email
    obj["password"]=password

    console.log(obj);
    userSignUpFun(obj)

}

async function userSignUpFun(obj){
    try {
        let res = await fetch(`http://localhost:8080/auth/register`, {
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
            let d = JSON.stringify(data);
            console.log(d);
            alert("customer registered successfuly")
            window.location.href = "./login.html";
        }
    } catch (error) {
        console.log(error)
        alert("failed to register")
    }
}