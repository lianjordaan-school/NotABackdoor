'use strict'

const textArea = document.querySelector(".box textarea");

textArea.addEventListener('keydown', function(event) {
	const openingBrackets = ['(', '[', '{', '"', "'", '`'];
	const closingBrackets = [')', ']', '}', '"', "'", '`'];

	if (event.key === 'Enter') {
		const text = textArea.value;
		const selectionStart = textArea.selectionStart;
		const selectionEnd = textArea.selectionEnd;
		const selectedText = text.substring(selectionStart, selectionEnd);
		const indentation = getIndentation(selectedText);

		// Get the text before and after the cursor
		const beforeCursor = text.substring(0, selectionStart);
		const afterCursor = text.substring(selectionEnd);

		// Find the nearest opening and closing brackets before and after the cursor
		const openingBracket = beforeCursor.lastIndexOf('{') > beforeCursor.lastIndexOf('[') ? beforeCursor.lastIndexOf('{') : beforeCursor.lastIndexOf('[');
		const closingBracket = afterCursor.indexOf('}') < afterCursor.indexOf(']') || afterCursor.indexOf('}') === -1 ? afterCursor.indexOf(']') : afterCursor.indexOf('}');

		// Check if there are brackets and if they are on the same line
		if (openingBracket !== -1 && closingBracket !== -1 && !/\n/.test(beforeCursor.substring(openingBracket, selectionStart)) && !/\n/.test(afterCursor.substring(0, closingBracket))) {
			const newLine = '\n\n' + indentation;
			event.preventDefault();
			textArea.value = beforeCursor + newLine + afterCursor;
			textArea.selectionStart = selectionEnd + indentation.length + 1;
			textArea.selectionEnd = textArea.selectionStart;
		}
	} else if (openingBrackets.includes(event.key)) {
		const start = textArea.selectionStart;
		const end = textArea.selectionEnd;
		if (start !== end) {
			event.preventDefault();
			const selectedText = textArea.value.substring(start, end);
			const wrappedText = event.key + selectedText + closingBrackets[openingBrackets.indexOf(event.key)];
			textArea.value = textArea.value.substring(0, start) + wrappedText + textArea.value.substring(end);
			textArea.selectionStart = start + 1;
			textArea.selectionEnd = end + 1;
		} else {
			event.preventDefault();
			const startPos = textArea.selectionStart;
			const endPos = textArea.selectionEnd;
			const text = textArea.value;
			const beforeCursor = text.substring(0, startPos);
			const afterCursor = text.substring(endPos);
			const bracket = getClosingBracket(event.key);

			textArea.value = beforeCursor + event.key + bracket + afterCursor;
			textArea.setSelectionRange(startPos + 1, startPos + 1);
		}

	} else if (event.key === 'Tab') {
		event.preventDefault();
		const startPos = textArea.selectionStart;
		const endPos = textArea.selectionEnd;
		const text = textArea.value;
		const beforeCursor = text.substring(0, startPos);
		const afterCursor = text.substring(endPos);
		const tab = '\t';
		textArea.value = beforeCursor + tab + afterCursor;
		textArea.setSelectionRange(startPos + 1, startPos + 1);
	}
});


function getClosingBracket(bracket) {
	switch (bracket) {
		case '{':
			return '}';
		case '(':
			return ')';
		case '[':
			return ']';
		case '"':
			return '"';
		case "'":
			return "'";
		case "`":
			return "`";
		default:
			return '';
	}
}

function getIndentation(text) {
	const match = text.match(/^\s*/);
	return match ? match[0] : '';
}

function countChars(str, char) {
	return str.split("").filter(c => c === char).length;
}


const lineIndicator = document.querySelector('.box div');
let last = -1,
	lastLines = 1,
	zero = 2;
const lineHeightValue = getComputedStyle(textArea).lineHeight;
const lineHeight = lineHeightValue === "normal" ? 1.2 * parseFloat(getComputedStyle(textArea).fontSize) : parseFloat(lineHeightValue);
const defaultWidth = lineIndicator.offsetWidth;

const updateLineIndicator = () => setTimeout(() => {
	const scrollHeight = textArea.scrollHeight,
		scrollTop = textArea.scrollTop,
		currentLine = Math.ceil(scrollTop / lineHeight),
		maxLines = Math.floor(textArea.clientHeight / lineHeight),
		totalLines = countChars(textArea.value, "\n") + 1;
	let top = Math.ceil(scrollTop / lineHeight) + 1;
	lastLines = totalLines, last = top, lineIndicator.innerHTML = "";
	lineIndicator.style.position = 'absolute';
	lineIndicator.style.top = `${textArea.offsetTop}px`;
	lineIndicator.style.lineHeight = `${lineHeight}px`;

	for (let i = top === 1 ? top : top - 1; i <= top + maxLines + 1; i++) {
		lineIndicator.scrollTop = scrollTop % lineHeight;
		lineIndicator.style.width = `2vw`;
		lineIndicator.style.height = `85vh`;

		if (i > totalLines && i <= top + maxLines - 1) break;

		const beforeWidth = lineIndicator.offsetWidth;

		for (let j = 1; j <= Math.floor(Math.log10(top + maxLines - 1)) - 1; j++) {
			lineIndicator.style.width = `${lineIndicator.offsetWidth * (7 / 6)}px`
		}

		const add = lineIndicator.offsetWidth !== beforeWidth ? (lineIndicator.offsetWidth - beforeWidth) * 1.2 : 0;
		lineIndicator.style.left = `${textArea.offsetLeft - 40 - add}px`;

		lineIndicator.innerHTML += i === currentLine ? `<span class="active" style="color: gray; font-size: 16.25px; font-family: 'Ubuntu';">${i}</span>` : `<span style="color: gray; font-size: 16.25px; font-family: 'Ubuntu';">${i}</span>`;
	}
}, 1);

[textArea, window].forEach(e => e.addEventListener('scroll', updateLineIndicator));
textArea.addEventListener('keydown', updateLineIndicator);
window.addEventListener('resize', updateLineIndicator);
updateLineIndicator();

posLoop();

function posLoop() {
	const save = document.querySelector(".save button");
	const savediv = document.querySelector(".save");

	savediv.style.top = `${textArea.offsetTop + textArea.offsetHeight}px`;
	savediv.style.left = `${textArea.offsetWidth - savediv.offsetWidth/5}px`;

	setTimeout(() => {
		posLoop();
	}, 10);
}

document.querySelector('.save').addEventListener('click', function() {
    // Get the value of 'Referer' header from the current request
    var url = window.location.href;

    // Replace the first occurrence of 'filemanager' with 'api/save'
    url = url.replace('filemanager', 'api/save-file');

    // Get the contents of the first 'textarea' element
    var body = document.querySelector('textarea').value;

    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Set the HTTP method and URL
    xhr.open('POST', url, true);

    // Set the Content-Type header to 'application/json'
    xhr.setRequestHeader('Content-Type', 'text/plain');

    // Set the body of the request
    xhr.send(body);

    // Add an event listener for the 'load' event
    xhr.addEventListener('load', function() {
        if (xhr.status === 201) {
            // Request was successful, handle response
            console.log('Request successful:', xhr.responseText);
        } else {
            // Request failed, handle error
            console.error('Request failed:', xhr.statusText);
        }
    });
});
