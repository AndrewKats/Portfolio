var studentArray;
var instructor = "";


// Array of duplicate ids (username) if any
var searchedDupliID = false;
var ids = {};
var duplicates ={};

var currentHighlight = "";

$(document).ready(function () {
  // Extract username of instructor from URL parameters and remove trailing slash
  try {
    instructor = url('?showStat').replace(/\/$/, "");
  }catch(e){};

  $('.title_h1').html("Welcome back " + instructor + "!");
  $(".welcome_div").text(function () {
    return $(this).text().replace("#instructor#", instructor);
  });

  // Access the csv file and pass to callback to process student data
  $.ajax({
    type: "GET",
    url: "data.csv",
    dataType: "text",
    success: function (data) { processStudents(data) }
  });

});

// Clears the text from search input
function clearSearch(){
  $('.search').val("");
}

// Searches for duplicate ids (usernames)
function searchDupli(){
  searchedDupliID = true;
  $('[id]').each(function (i) {
    if (this.id && ids[this.id]){
      duplicates[this.id] = 1;
    }
    else {
      ids[this.id] = 1;
    }
  });
}

// Highlights the proper entries in the table. Called from search input
function highlightRowSearch() {
  // Find all duplicate ids if haven't already done so
  if (!searchedDupliID){
    searchDupli();
  }
  // Get the search term
  searchTerm = $('.search').val().toLowerCase();

  // Un-highlight what is currently highlighted
  $(currentHighlight).removeClass("highlight");

  // If the search term is a unique username highlight and jump to entry
  if (ids[searchTerm] && !duplicates[searchTerm]){
    location.hash = "#" + searchTerm;
    currentHighlight = "#" + searchTerm;
    $(currentHighlight).addClass("highlight");
  }
  // Otherwise highlight all entries matching the search term
  else{
    currentHighlight = "." + searchTerm;
    $(currentHighlight).addClass("highlight");
  }
}

// Highlights the proper entry in table, called from extra graph info
function highlightRow(id) {
  location.hash = "#" + id.toLowerCase();;
  $(currentHighlight).removeClass("highlight");
  currentHighlight = "#" + id.toLowerCase();;
  $(currentHighlight).addClass("highlight");
}

// Adds extra information above graph when slice is clicked.
function showSelectedSudent(data) {
  highlightRow(data.id);

  $(".individual-info").addClass("bigger-text");
  var individualText = '<span class="indiv-info">'
  + data.id
  + "</span> has a total score of "
  + '<span class="indiv-info">' + data.score
  + "</span> and has completed "
  + '<span class="indiv-info">' + data.level + "</span>"
  + " levels</br>"
  + '<a href="#' + data.id + '"'
  + '" onclick="highlightRow(\'' + data.id + '\')" class="more-info">'
  + 'More Info' + '</a>';
  $(".individual-info").empty();
  $(".individual-info").html(individualText);
}

// Confirms deletion of a student and sends a query to the server using ajax
function deleteUser(teacher, user) {
  var errorMessage = "Are you sure you want to delete student " + user;
  if (confirm(errorMessage)) {
    $.ajax({
      type: "GET",
      url: "/delete/?teacher=" + teacher + "&student=" + user,
      dataType: "text",
      success: function (data) { ; }
    });
  }
}

// Populates the student array from csv file, generates the tables and adds it to DOM
function processStudents(data) {
  studentArray = $.csv.toObjects(data);
  var table = generateTable(studentArray);
  // getHighestScore(studentArray);
  $(".results").empty();
  $(".results").html(table);
}

// Converts an array of objects to HTML tabluar format
function generateTable(data) {
  var html = '';
  if (typeof (data[0]) === 'undefined') {
    return null;
  }
  if (data[0].constructor === Object) {
    for (var row in data) {
      html += '<tr id="' + (data[row].id).toLowerCase() +  '" class="' + (data[row].id).toLowerCase() +" " +(data[row].fullName).toLowerCase() + " " + data[row].score + " " + (data[row].currentlevel).toLowerCase() + '">\r\n';

      html +=
      '<td class="delete-student"">'
      + '<a class="confirm" href="#'
      + '" onclick="return deleteUser(\'' + instructor + '\',\'' + data[row].id + '\')">'
      + '<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Red_x.svg/1024px-Red_x.svg.png" alt="Smiley face" height="8" width="8"></a></td>';

      for (var item in data[row]) {
        var columnHeader = "";
        switch (item) {
          case "currentlevel":
          columnHeader = "Current Level";
          break;
          case "id":
          columnHeader = "Username";
          break;
          default:
          columnHeader = item;
        }
        html += '<td><b class="cell-title">' + columnHeader + '</b>:' + data[row][item] + '</td>\r\n';
      }
      html += '</tr>\r\n';
    }
  }
  return html;
}
