
Element.prototype.addClass = function(c) {
    if (this.hasAttribute("class")) {
        this.setAttribute("class", this.getAttribute("class") + " " + c + " ");
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

    var timePickerInput = document.querySelector("#timePickerInput");
    var timePicker = document.querySelector("#timePicker");
    var hoursUp = document.querySelector("#hoursUp");
    var hoursValue = document.querySelector("#hoursValue");
    hoursValue.val = 0;
    var hoursDown = document.querySelector("#hoursDown");
    var minutesUp = document.querySelector("#minutesUp");
    var minutesValue = document.querySelector("#minutesValue");
    minutesValue.val = 0;
    var minutesDown = document.querySelector("#minutesDown");

    document.addEventListener("click", function(e) {
        if (e.target !== timePicker && timePicker.getAttribute("class").indexOf("show") !== -1) {
            timePicker.removeClass("show");
        }
    });

    timePickerInput.addEventListener("click", function(e) {
        timePickerInput.value = "00:00";
        timePicker.toggleClass("show");
        e.stopPropagation();
    });

    hoursUp.addEventListener("click", function(e) {
        hoursValue.val >= 23 ? hoursValue.val = 0 : hoursValue.val++;
        hoursValue.val <= 9 ? hoursValue.innerHTML = "0" + hoursValue.val : hoursValue.innerHTML = hoursValue.val;
        timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
        e.stopPropagation();
    });

    hoursDown.addEventListener("click", function(e) {
        hoursValue.val <= 0 ? hoursValue.val = 23 : hoursValue.val--;
        hoursValue.val <= 9 ? hoursValue.innerHTML = "0" + hoursValue.val : hoursValue.innerHTML = hoursValue.val;
        timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
        e.stopPropagation();
    });

    minutesUp.addEventListener("click", function(e) {
        minutesValue.val >= 59 ? minutesValue.val = 0 : minutesValue.val++;
        minutesValue.val <= 9 ? minutesValue.innerHTML = "0" + minutesValue.val : minutesValue.innerHTML = minutesValue.val;
        timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
        e.stopPropagation();
    });

    minutesDown.addEventListener("click", function(e) {
        minutesValue.val <= 0 ? minutesValue.val = 59 : minutesValue.val++;
        minutesValue.val <= 9 ? minutesValue.innerHTML = "0" + minutesValue.val : minutesValue.innerHTML = minutesValue.val;
        timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
        e.stopPropagation();
    });

};
