var content = document.querySelector(".nb-content");
var navbar = document.querySelector(".navbar");

document.querySelector(".nb-arrow").addEventListener("mouseover", () => {

    content.style.left = "0px";

});

content.addEventListener("mouseout", (e) => {
    const toElement = e.toElement || e.relatedTarget;
    if (!content.contains(toElement)) {
        content.style.left = "-20vw";
    }



});

var selects = document.querySelectorAll(".nb-select");

selects.forEach((i) => {

    i.addEventListener("click", () => {

        window.location.href = i.dataset.redirect

    });

});