/**
 * Created by bilbowm on 17/11/2015.
 */
function startRabbitServer() {
    var amqp = require('libs/amqplib/callback_api');
    amqp.connect('amqp://localhost', function(err, conn) {

        conn.createChannel(function(err, ch) {
            var q = 'websockets';

            ch.assertQueue(q, {durable: false});
        });
    });
    console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q);
    ch.consume(q, function(msg) {
        console.log(" [x] Received %s", msg.content.toString());
    }, {noAck: true});
}