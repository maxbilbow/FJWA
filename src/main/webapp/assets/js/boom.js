/**
 * Created by bilbowm on 26/11/2015.
 */
require(['jquery', 'rmxjs/rmx-sockets', 'rmxjs/pubsub', 'boom/bomb'], function ($, WSHelper, $ps, Bomb) {
    function getContextPath() {
        return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    }

    var fjwa = {
        bombs: null,
        showAll: false,
        root: getContextPath()
    };



    var updateBombList = function (data) {
        if (data) {
            if (fjwa.bombs == null) {
                fjwa.bombs = {};
                fetchList();
                return;
            }
            var newList= {};
            var len = data.length;
            $('#bomb-list').html('');
            var listIsComplete = true;
            for (var i = 0; i < len; i++) {
                var jBomb = data[i];
                if (jBomb.id) { //Wait for IDs
                    newList[jBomb.id] = new Bomb(jBomb);
                } else {
                    listIsComplete = false;
                }
            }
            if (listIsComplete) { //if the list was not complete, check the server later.
                fjwa.bombs = newList;
            } else {
                setTimeout(function() {
                    fetchList();
                },3000);
                return;
            }
        }
        for (var id in fjwa.bombs) {
            var bomb = fjwa.bombs[id];
            var entry = $('#bomb-list p#' + bomb.id);
            if (entry.length < 1) {
                entry = $(document.createElement('p')).attr('id', bomb.id);
                $('#bomb-list').append(entry);
            }
            entry.html(bomb.getDescription());
        }
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

    function filter() {
        if (fjwa.showAll == false) {
            $('#bomb-list p').filter(function () {
                var id = $(this).attr('id');
                var b = fjwa.bombs[id];
                var result = !b || !b.isLive();
                return result;
            }).hide();
        } else {
            $('#bomb-list p').show();
        }
    }

    $('#show-expired').click(function toggleBombs() {
        fjwa.showAll = !fjwa.showAll;
        filter();
        $('#show-expired').html(fjwa.showAll ? "Hide Expired" : "Show Expired");
    });


    function fetchList (){
        $.getJSON(fjwa.root + '/updateBombs.json', {
            ajax: 'true'
        }, function (data) {
            updateBombList(data);
        });
    }
    fetchList();

    (function update() {
        //console.log("Updating...");
        setTimeout(function() {
            updateBombList();
            update();//repeat on completion?
        }, 1000);
    })();
});