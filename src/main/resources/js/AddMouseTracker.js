var wrapper = document.createElement('div');
var tracker = document.createElement('div');
wrapper.id = "at-mouse-tracker-wrapper";
tracker.id = "at-mouse-tracker";
wrapper.appendChild(tracker);
document.body.appendChild(wrapper);
document.body.onmousemove = function(e) {
    tracker.style.left = e.clientX+"px";
    tracker.style.top = e.clientY+"px";
};
