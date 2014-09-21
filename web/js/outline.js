
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


function createDatePickerHead() {
    var thead = document.createElement("thead");
    // ptimo tr
    var tr = document.createElement("tr");
    // primo th
    var th = document.createElement("th");
    th.setAttribute("id", "previousMonth");
    var span = document.createElement("span");
    span.setAttribute("class", "arrow_carrot-2left");
    th.appendChild(span);
    tr.appendChild(th);
    // secondo th
    th = document.createElement("th");
    th.setAttribute("id", "monthAndYear");
    th.setAttribute("colspan", "5");
    // primo span
    span = document.createElement("span");
    span.setAttribute("id", "month");
    th.appendChild(span);
    // secondo span
    span = document.createElement("span");
    span.setAttribute("id", "year");
    th.appendChild(span);
    tr.appendChild(th);
    // terzo th
    th = document.createElement("th");
    th.setAttribute("id", "nextMonth");
    span = document.createElement("span");
    span.setAttribute("class", "arrow_carrot-2right");
    th.appendChild(span);
    tr.appendChild(th);
    // fine primo tr
    thead.appendChild(tr)
    // secondo tr
    tr = document.createElement("tr");


}


function createDatePickerStructure() {
    var datePicker = document.createElement("div");
    datePicker.setAttribute("id", "datePicker");
    datePicker.setAttribute("class", "row");
    var table = document.createElement("table");
    table.appendChild(createDatePickerHead());
    table.appendChild(createDatePickerBody());
    datePicker.appendChild(table);
    return datePicker;
}



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


    // TIME PICKER
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
        minutesValue.val <= 0 ? minutesValue.val = 59 : minutesValue.val--;
        minutesValue.val <= 9 ? minutesValue.innerHTML = "0" + minutesValue.val : minutesValue.innerHTML = minutesValue.val;
        timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
        e.stopPropagation();
    });



    // DATE PICKER
    var datePickerInput = document.querySelector("#datePickerInput");
    var datePicker = document.querySelector("#datePicker");
    var year = document.querySelector("#year");
    var month = document.querySelector("#month");
    var days = document.querySelectorAll("#datePicker td.day");
    var previousMonth = document.querySelector("#previousMonth");
    var nextMonth = document.querySelector("#nextMonth");

    datePickerInput.addEventListener("click", function(e) {
        var now = new Date();
        month.val = now.getMonth();
        month.innerHTML = getStringMonth(month.val);
        year.val = now.getFullYear();
        year.innerHTML = year.val;
        createDatePicker(days, month.val, year.val);
        e.stopPropagation();
    });

    nextMonth.addEventListener("click", function(e) {
        if (month.val == 11) {
            month.val = 0;
            year.val++;
        } else {
            month.val++;
        }
        month.innerHTML = getStringMonth(month.val);
        year.innerHTML = year.val;
        createDatePicker(days, month.val, year.val);
        e.stopPropagation();
    });

    previousMonth.addEventListener("click", function(e) {
        if (month.val == 0) {
            month.val = 11;
            year--;
        } else {
            month.val--;
        }
        month.innerHTML = getStringMonth(month.val);
        year.innerHTML = year.val;
        createDatePicker(days, month.val, year.val);
        e.stopPropagation();
    });

    for (var i = 0; i < days.length; i++) {
        days[i].addEventListener("click", function(e) {
            datePickerInput.value = e.target.val.getDate() + "/" + (e.target.val.getMonth() + 1) + "/" + e.target.val.getFullYear();
            e.stopPropagation();
        });
    }



    function createDatePicker(nodeList, month, year) {
        var firstOfMonth = new Date(year, month, 1);
        for (var i = 0; i < nodeList.length; i++) {
            nodeList[i].val = new Date(firstOfMonth.getTime() - ((firstOfMonth.getDay() - i - 1) * 24 * 60 * 60 * 1000));
            nodeList[i].innerHTML = nodeList[i].val.getDate();
        }
    }

    function getStringMonth(month) {
        switch (month) {
            case 0:
                return "Gennaio";
                break;
            case 1:
                return "Febbraio";
                break;
            case 2:
                return "Marzo";
                break;
            case 3:
                return "Aprile";
                break;
            case 4:
                return "Maggio";
                break;
            case 5:
                return "Giugno";
                break;
            case 6:
                return "Luglio";
                break;
            case 7:
                return "Agosto";
                break;
            case 8:
                return "Settembre";
                break;
            case 9:
                return "Ottobre";
                break;
            case 10:
                return "Novembre";
                break;
            case 11:
                return "Dicembre";
                break;

        }
    }


};
