const fileDivs = document.querySelectorAll(".file");
fileDivs.forEach((fileDiv) => {
  fileDiv.addEventListener("click", () => {
    const fileLink = fileDiv.querySelector("a").href;
    window.open(fileLink, "_self");
  });
});

document.querySelector(".back").addEventListener("click", () => {
  const url = window.location.href.split("/");
  url.pop();
  url.pop();
  window.location.href = url.join("/") + "/";
});

const fileDownloads = document.querySelectorAll(".download");
fileDownloads.forEach((fileDownload) => {
    fileDownload.addEventListener('click', function(event) {
        event.preventDefault();
        event.stopPropagation();
        const url = fileDownload.dataset.href;
        window.open(url, '_self');
    });
});

