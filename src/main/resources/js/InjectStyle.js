var cssText = arguments[0];
var cssId = arguments[1];

var head = document.head || document.getElementsByTagName('head')[0];
var style = document.createElement('style');
style.id = cssId;
style.type = 'text/css';
if (style.styleSheet) {
    style.styleSheet.cssText = cssText;
} else {
    style.appendChild(document.createTextNode(cssText));
}
head.appendChild(style);