
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

var searchSpan = document.querySelector("#searchSpan");
var searchForm = document.querySelector("#searchForm");
searchForm.addClass("hide");
searchSpan.addEventListener("click", function(e) {
    searchForm.toggleClass("hide");
    searchForm.toggleClass("show");

});
