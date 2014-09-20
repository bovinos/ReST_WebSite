
Element.prototype.addClass = function(c) {
    if (this.hasAttribute("class")) {
        this.setAttribute("class", this.getAttribute("class") + c + " ");
    } else {
        this.setAttribute("class", c + " ");
    }
};

Element.prototype.removeClass = function(c) {
    if (this.hasAttribute("class")) {
        classArray = this.getAttribute("class").split(" ");
        classArray.splice(classArray.indexOf(c), 1);
        newClass = classArray.join(" ");
        this.setAttribute("class", newClass);
    }
};

Element.prototype.toggleClass = function(c) {
    if (this.hasAttribute("class") && this.getAttribute("class").indexOf(c) !== -1) {
        this.removeClass(c);
    } else {
        this.addClass(c);
    }
};

window.onload = function() {

    // tolgiamo la possibilità di poter selezionare il testo delle label
    // in modo da far funzionare al meglio le checkbox
    var elements = document.querySelectorAll("label");
    for (var i = 0; i < elements.length; i++) {
        elements[i].onselectstart = function() {
            return false;
        }; // explorer
        elements[i].onmousedown = function() {
            return false;
        }; // mozilla & chrome
    }

    var timePicker = document.querySelector("#timePicker");
    var hoursUp = document.querySelector("#hoursUp");
    var hoursValue = document.querySelector("#hoursValue");
    var hoursDown = document.querySelector("#hoursDown");
    var minutesUp = document.querySelector("#minutesUp");
    var minutesValue = document.querySelector("#minutesValue");
    var minutesDown = document.querySelector("#minutesDown");


};
