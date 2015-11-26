/**
 * Created by bilbowm on 26/11/2015.
 */
require(['jquery', 'rmxjs/rmx-sockets', 'rmxjs/pubsub'], function ($, WSHelper, $ps) {
    function getContextPath() {
        return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    }

    var fjwa = {
        bombs: null,
        showAll: false,
        root: getContextPath()
    };

    var updateBombList = function (data) {
        var html = '';
        if (data != null) {
            fjwa.bombs = {};
            var len = data.length;
            for (var i = 0; i < len; i++) {
                fjwa.bombs[data[i].id] = data[i];
                //if (fjwa.showAll || ( data[i].live && !data[i].outOfTime ))
                html += '<span id="' + data[i].id + '"><br/>' + data[i].description + '</span>';//.toString
            }
        } else {
            html += "<br/>NULL";
        }
        $('#bomb-list').html(html);
        filter();
    };

    $('#defuse').click(function defuse() {
        $.getJSON(fjwa.root + '/defuse.json', {
                ajax: 'true'
            }, updateBombList
        );

    });

    $('#add-bomb').click(function addBomb() {
        $.getJSON(fjwa.root + '/addBomb.json', {
                ajax: 'true'
            }, updateBombList
        );
    });

    $('#remove-all').click(function removeAll() {
        $.getJSON(fjwa.root + '/removeAll.json', {
            ajax: 'true'
        }, updateBombList);
    });

    function filter(){
        if (fjwa.showAll == false) {
            $('#bomb-list span').filter(function () {
                var id = $(this).attr('id');
                var b = fjwa.bombs[id];
                var result = !b || !b.live || b.outOfTime;
                return result;
            }).hide();
        } else {
            $('#bomb-list').show().filter(function(){return true});
        }
    }
    $('#show-expired').click(function toggleBombs() {
        fjwa.showAll = !fjwa.showAll;
        filter();
        $('#show-expired').html(fjwa.showAll ? "Hide Expired" : "Show Expired");
    });




    (function update() {
        //console.log("Updating...");
        setTimeout(function updateFromServer() {
            $.getJSON(fjwa.root + '/updateBombs.json', {
                ajax: 'true'
            }, function (data) {
                updateBombList(data);
                update();//repeat on completion?
            });
        }, 1000);
    })();
});