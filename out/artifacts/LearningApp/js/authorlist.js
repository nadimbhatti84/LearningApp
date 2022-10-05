/**
 * view-controller for authorlist.html
 * @author Marcel Suter
 */
let delayTimer;
const userRole = getCookie("userRole");

document.addEventListener("DOMContentLoaded", () => {
    showNav(userRole);
    readAuthors("","");

    document.getElementById("search").addEventListener("keyup", searchAuthors);
});

/**
 * look up the search-fields and create the filter
 * @param event
 */
function searchAuthors(event) {
    const element = event.target;
    const field = element.id;
    let filter = "";
    filter = document.getElementById("authorName").value;

    clearTimeout(delayTimer);
    delayTimer = setTimeout(() => {
        readAuthors(field, filter);
    }, 500);
}
/**
 * shows the authors as a table
 * @param data  the authors
 */
function showAuthorList(data) {
    let tBody = document.getElementById("authorlist");
    tBody.innerHTML = "";
    data.forEach(author => {
        let row = tBody.insertRow(-1);
                let button = document.createElement("button");
        if (userRole === "user" || userRole === "admin")
            button.innerHTML = "&#9998;";
        else
            button.innerHTML = "&#128065;";

        button.type = "button";
        button.name = "editBook";
        button.setAttribute("data-authoruuid", author.authorUUID);
        button.addEventListener("click", editAuthor);
        row.insertCell(-1).appendChild(button);

        row.insertCell(-1).innerHTML = author.authorName;

        if (userRole === "admin") {
            button = document.createElement("button");
            button.innerHTML = "&#128465;";
            button.type = "button";
            button.name = "deleteBook";
            button.setAttribute("data-authoruuid", author.authorUUID);
            button.addEventListener("click", deleteAuthor);
            row.insertCell(-1).appendChild(button);
        }

    });

    if (userRole === "user" || userRole === "admin") {
        document.getElementById("addButton").innerHTML = "<a href='./authoredit.html'>Neuer Autor</a>";
    }
}

/**
 * redirects to the edit-form
 * @param event  the click-event
 */
function editAuthor(event) {
    const button = event.target;
    const authorUUID = button.getAttribute("data-authoruuid");
    window.location.href = "./authoredit.html?uuid=" + authorUUID;
}

/**
 * deletes a author
 * @param event  the click-event
 */
function deleteAuthor(event) {
    const button = event.target;
    const authorUUID = button.getAttribute("data-authoruuid");

    fetch("./resource/author/delete?uuid=" + authorUUID,
        {
            method: "DELETE",
            headers: { "Authorization": "Bearer " + readStorage("token")}
        })
        .then(function (response) {
            if (response.ok) {
                window.location.href = "./authorlist.html";
            } else if (response.status === 401) {
                window.location.href = "./";
            } else {
                console.log(response);
            }
        })
        .catch(function (error) {
            console.log(error);
        });
}