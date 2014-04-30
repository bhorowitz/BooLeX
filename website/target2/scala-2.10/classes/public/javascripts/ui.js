$(document).ready(function() {
	$('button#start-button').click(function(e) {
		$('#echoer').attr('src', "/echo/" + encodeURIComponent(Gate.createDSL()));
	});

	$('button#stop-button').click(function(e) {
		$('#echoer').attr('src', "/null");
	});

	$(window).on('refreshDSL', function(e) {
		$('pre#dsl').text(Gate.createDSL());
	});
});