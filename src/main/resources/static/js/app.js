function ScreenCaster(config) {

  this.enabled = config.enabled === true;
  this.watcher = config.watcher === true;
  this.showUpdates = true;
  this.stompClient = null;
  this.screenUpdateInterval = 250;
  this.currentPointerLocation = null;
  this.notificationStatus = {
    enabed: false
  };
  this.headers = {};

  this.init = function init() {

    this.headers["X-CSRF-TOKEN"] = $("meta[name=csrf]").attr("value");

    this.initWebSocketConnection();

    this.initNotes();

    if (this.watcher) {
      this.initScreenCast();
      return;
    }

    this.notificationStatus.enabled = false;
    this.setupNotesForm();
    this.initClipboardSupport();
  };

  this.initNotes = function initNotes() {

    this.loadNotes();

    $('#notesListContainer').perfectScrollbar();
  }.bind(this);

  this.initScreenCast = function initScreenCast() {

    this.$screenImage = $("#screen")[0];
    this.$overlay = $("#overlay")[0];

    this.initScreenVisibilityHandling();
    this.initNotifications();
    this.initResizeTools();

  }.bind(this);

  this.start = function start() {

    this.startScreenCast();
    this.startPointerAnimation();

  }.bind(this);

  this.initWebSocketConnection = function initWebSocketConnection() {

    var socket = new SockJS("/screencaster/ws");
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null;

    this.stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);

      this.stompClient.subscribe("/topic/notes", function (noteMessage) {
        this.onNoteEvent(JSON.parse(noteMessage.body));
      }.bind(this));

      this.stompClient.subscribe("/topic/settings", function (settingsMessage) {
        this.onSettingsEvent(JSON.parse(settingsMessage.body));
      }.bind(this));

      this.stompClient.subscribe("/topic/pointer", function (pointerMessage) {
        this.onPointerEvent(JSON.parse(pointerMessage.body));
      }.bind(this));

    }.bind(this));

  }.bind(this);

  this.onPointerEvent = function onPointerEvent(pointerEvent) {
    // console.log(pointerEvent);
    this.currentPointerLocation = pointerEvent;
  }.bind(this);

  this.onSettingsEvent = function onSettingsEvent(settingsEvent) {

    if (settingsEvent.type === "updated") {

      var enabledChanged = this.enabled !== settingsEvent.settings.castEnabled;
      this.enabled = settingsEvent.settings.castEnabled;

      if (this.enabled && enabledChanged) {
        this.startScreenCast();
      }

      $("#screenCastStatus").text(this.enabled ? "active" : "not active");
    }
  }.bind(this);

  this.scrollToLatestNote = function scrollToLatestNote() {
    var $notesListContainer = $("#notesListContainer");
    $notesListContainer.animate({scrollTop: $notesListContainer.prop("scrollHeight")}, 250);
  };

  this.onNoteEvent = function onNoteEvent(noteEvent) {

    if (noteEvent.type === "created") {

      this.addNote(noteEvent.note);

      this.scrollToLatestNote();

      if (this.notificationStatus.enabled) {

        var notification = new Notification("New notes", {
          body: "there are new notes available",
          icon: "/img/new-note-icon-128x128.png",
          timestamp: Date.now()
        });

        setTimeout(notification.close.bind(notification), 3000);
      }

    } else if (noteEvent.type === "deleted") {
      $("li[data-note-id='" + noteEvent.noteId + "']").remove();
    }

    this.updateUnreadNotesCount();
  }.bind(this);

  this.addNote = function addNote(note) {

    var template = $('#note-template').html();
    Mustache.parse(template);   // optional, speeds up future uses

    note.createdAtHuman = moment(note.createdAt).format("DD.MM.YY HH:mm:ss");

    var rendered = Mustache.render(template, note).trim();

    $("#notesList").append(rendered);
  };

  this.updateUnreadNotesCount = function updateUnreadNotesCount() {
    $("#unreadNotesCounter").text($(".note.new").length);
  };

  this.updateNote = function updateNote(event) {

    event.preventDefault();

    if (event.currentTarget.value === 'delete') {

      $.ajax({
        url: $(event.target.form).attr("action"),
        type: "delete",
        headers: this.headers
      }).done(function (response) {
        console.log("note deleted");
      });

      return;
    }

    if (event.currentTarget.value === 'deleteAll') {

      var proceed = window.confirm("Delete all notes?");

      if (!proceed) {
        return;
      }

      $.ajax({
        url: "/notes",
        type: "delete",
        headers: this.headers
      }).done(function (response) {
        console.log("all notes deleted");

        $("li[data-note-id]").remove();
      });
    }
  }.bind(this);

  this.markNoteAsRead = function markNoteAsRead(note) {

    var $note = $(note);
    $note.removeClass('new');
    $note.addClass('read');

    this.updateUnreadNotesCount();
  }.bind(this);


  this.loadNotes = function loadNotes() {

    $.getJSON("/notes", function (notes) {
      for (var i = 0; i < notes.length; i++) {
        this.addNote(notes[i]);
      }
    }.bind(this));
  }.bind(this);

  this.setupNotesForm = function setupNotesForm() {
    $("#notesForm").submit(function (event) {

      event.preventDefault();

      var note = {
        text: $("#txtNote").val()
      };

      this.storeNote(note);

      $("#notesForm")[0].reset();
    }.bind(this));
  }.bind(this);

  this.storeNote = function storeNote(note) {

    $.ajax({
      url: "/notes",
      type: "post",
      data: note,
      headers: this.headers
    }).done(function (response) { //
      // console.log(response);
    });
  }.bind(this);

  this.initClipboardSupport = function initClipboardSupport() {

    function onPaste(evt) {

      if (!evt.clipboardData) {
        return;
      }

      var items = evt.clipboardData.items;
      if (!items) {
        return;
      }

      if (items.length === 0) {
        return;
      }

      var currentItem = items[0];
      if (currentItem.type.indexOf("image") === -1) {
        return;
      }

      var blob = currentItem.getAsFile();

      this.uploadFile({
        filename: "Screenshot " + moment(Date.now()).format("DD-MM-YY_HH-mm-ss"),
        data: blob,
        contentType: currentItem.type
      }, function (fileInfo) {

        if (!fileInfo) {
          return;
        }

        this.storeNote({
          text: "### " + fileInfo.name + "\n" +
          "![Screenshot](/files/" + fileInfo.id + ")"
        });
      }.bind(this));

      evt.preventDefault();
    }

    document.addEventListener('paste', onPaste.bind(this), false);
  }.bind(this);

  this.uploadFile = function uploadFile(fileData, callback) {

    var data = new FormData();
    data.append('file', fileData.data, fileData.filename);

    $.ajax({
      url: "/files",
      type: "post",
      enctype: 'multipart/form-data',
      data: data,
      processData: false,
      contentType: false,
      headers: this.headers,
      success: callback
    });
  }.bind(this);

  this.initScreenVisibilityHandling = function initScreenVisibilityHandling() {

    var hiddenAttributeName, visibilityChangeEventName;
    if (typeof document.hidden !== "undefined") { // Opera 12.10 and Firefox 18 and later support
      hiddenAttributeName = "hidden";
      visibilityChangeEventName = "visibilitychange";
    } else if (typeof document.msHidden !== "undefined") {
      hiddenAttributeName = "msHidden";
      visibilityChangeEventName = "msvisibilitychange";
    } else if (typeof document.webkitHidden !== "undefined") {
      hiddenAttributeName = "webkitHidden";
      visibilityChangeEventName = "webkitvisibilitychange";
    } else {
      // step out, since some older browsers don't support handling of visibility changes.
      return;
    }

    var handler = function () {

      if (document[hiddenAttributeName]) {
        document.title = "Paused...";
        console.log("stop updating");
        this.showUpdates = false;
      } else {
        document.title = "Active...";
        console.log("start updating");
        this.showUpdates = true;

        this.startScreenCast();
      }
    }.bind(this);

    document.addEventListener(visibilityChangeEventName, handler, false);
  }.bind(this);

  this.initNotifications = function initNotifications() {

    if (!("Notification" in window)) {
      console.log("This browser does not support system notifications");
      this.notificationStatus.enabled = false;
      return;
    }

    if (Notification.permission === "granted") {
      this.notificationStatus.enabled = true;
      return;
    }

    if (Notification.permission !== 'denied') {
      Notification.requestPermission(function (permission) {
        if (permission === "granted") {
          this.notificationStatus.enabled = true;
        }
      }.bind(this));
    }
  }.bind(this);

  this.initResizeTools = function initResizeTools() {

    // TODO fix resizing

    $("#screenContainer").onclick = function (evt) {
      $(this.$screenImage).toggleClass("screen-fit")
    };
  }.bind(this);

  /**
   * Starts the screenshot fetching
   *
   * @type {any}
   */
  this.startScreenCast = function startScreenCast() {

    if ("URLSearchParams" in window) {
      var urlParams = new URLSearchParams(window.location.search);
      if (urlParams.has("screenUpdateInterval")) {
        this.screenUpdateInterval = parseInt(urlParams.get("screenUpdateInterval"), 10);
      }
    }

    if (!this.$screenImage) {
      return;
    }

    this.$screenImage.onload = (function () {

      if (!this.enabled) {
        return;
      }

      if (!this.casterScreenDimensions) {

        // resize canvas...
        this.casterScreenDimensions = {
          w: this.$screenImage.naturalWidth,
          h: this.$screenImage.naturalHeight
        };

        this.$overlay.width = this.$screenImage.width;
        this.$overlay.height = this.$screenImage.height;
      }

      setTimeout(refreshImage.bind(this), this.screenUpdateInterval);
    }).bind(this);

    function refreshImage() {

      if (!this.showUpdates) {
        return;
      }

      console.log("screen update");

      this.$screenImage.src = "/screenshot.jpg?" + Date.now();
    }

    setTimeout(refreshImage.bind(this), this.screenUpdateInterval);
  }.bind(this);

  /**
   * Renders the remote mouse pointer
   */
  this.startPointerAnimation = function startPointerAnimation() {

    requestAnimationFrame(renderLoop.bind(this));

    var startTime = Date.now();
    var pulseDuration = 1.45;

    function renderLoop() {

      let pointerLocation = this.currentPointerLocation;
      let screenDimensions = this.casterScreenDimensions;

      if (pointerLocation && screenDimensions) {

        var time = (Date.now() - startTime) / 1000.0;

        var pulseCompletion = (time % pulseDuration) / pulseDuration;

        var cvs = this.$overlay;

        var context = cvs.getContext('2d');
        context.globalAlpha = 0.95;

        context.clearRect(0, 0, cvs.width, cvs.height);

        var scalingW = cvs.width / screenDimensions.w;
        var scalingH = cvs.height / screenDimensions.h;

        var centerX = cvs.width / 2;
        var centerY = cvs.height / 2;
        var radius = 4;

        context.beginPath();
        context.arc(pointerLocation.x * scalingW, pointerLocation.y * scalingH, radius, 0, 2 * Math.PI, false);
        context.fillStyle = 'magenta';
        context.fill();

        context.beginPath();
        context.arc(pointerLocation.x * scalingW, pointerLocation.y * scalingH, radius + pulseCompletion * 10, 0, 2 * Math.PI, false);

        context.lineWidth = 1;
        context.strokeStyle = 'magenta';
        context.globalAlpha = 1 - pulseCompletion;
        context.stroke();
      }

      requestAnimationFrame(renderLoop.bind(this));
    }
  }
}