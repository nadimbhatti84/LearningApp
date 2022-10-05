/**
 * reads all authors
 * @param field   the attribute to be used as a filter or sort
 * @param filter  the value to be filtered by
 * @param sort    the sort order
 */
function readAuthors(field, filter, sort) {
    let url = "./resource/author/list";
    if (field !== "" && filter !== "") {
        url += "?field=" + field;
        url += "&filter=" + filter;
    }
    fetch(url, {
        headers: { "Authorization": "Bearer " + readStorage("token")}
    })
        .then(function (response) {
            if (response.ok) {
                return response;
            } else if (response.status === 401) {
                window.location.href = "./";
            } else {
                console.log(response);
            }
        })
        .then(response => response.json())
        .then(data => {
            showAuthorList(data);
        })
        .catch(function (error) {
            console.log(error);
        });
}