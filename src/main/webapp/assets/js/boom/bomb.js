/**
 * Created by bilbowm on 26/11/2015.
 */
'use strict';
define(function () {
    function now() {
        return +new Date() / 1000;//.getSeconds();
    }

    var Bomb = function newBomb(obj) {

        if (obj) {
            for (var k in obj) {
                if (!this[k]) { //do not override prototype.
                   this[k] = obj[k];
                }
            }
        }
        //if (obj.startTime.epochSecond)
        //    this.timeRemaining = now() - this.startTime.epochSecond;
        return this;
    };

    //Bomb.prototype = Object.create(Bomb.prototype);
    Bomb.prototype.diffusable = null;
    Bomb.prototype.live = null;
    Bomb.prototype.startTime = null;
    Bomb.prototype.startTimeInSeconds = null;
    Bomb.prototype.id = null;
    Bomb.prototype.description = null;
    Bomb.prototype.timeRemaining = null;
    Bomb.prototype.getTimeRemaining = function () {
        if (!this.live) {
            return this.timeRemaining || this.startTimeInSeconds;
        }
        var dt = now() - this.startTime.epochSecond;// / 1000;
        var rem = this.startTimeInSeconds - dt;
        return this.timeRemaining = rem > 0 ? Math.round(rem) : 0;
    };

    Bomb.prototype.isOutOfTime = function () {
        return this.getTimeRemaining() <= 0;
    };

    Bomb.prototype.getDescription = function () {
        this.description = "Bomb " + this.name + ": ";
        if (this.isOutOfTime())
            this.description += "<span style=\"color: red;\">BOOM!</span>";
        else if (!this.live)
            this.description += "<span style=\"color: green;\">Diffused with " + this.getTimeRemaining() + " remaining.</span>";
        else
            this.description += this.getTimeRemaining() + " seconds!";
        return this.description;
    };

    Bomb.prototype.isLive = function () {
        return this.live && !this.isOutOfTime();
    };

    Bomb.prototype.setLive = function (live) {
        if (this.diffusable || live) {
            //this.setStartTimeInSeconds(this.getTimeRemaining());
            this.live = live;
        }
    };

    //Bomb.prototype.setStartTimeInSeconds = function (timeInSeconds) {
    //    this.startTimeInSeconds = timeInSeconds > 0 ? timeInSeconds : 0;
    //    this.startTime = new Date();
    //};

    return Bomb;
});