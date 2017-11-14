var width = 500,
height = 500,
radius = Math.min(width, height) / 2,
innerRadius = 0.3 * radius;

// Highest score to properly adjust the graph radius
var highestScore = 0;

var pie = d3.layout.pie()
.sort(null)
.value(function (d) { return d.width; });

var tip = d3.tip()
.attr('class', 'd3-tip')
.offset([0, 0])
// Show slice info
.html(function (d) {
  return d.data.id + ": <span style='color:orangered'>" + d.data.score + ", Level " + d.data.level + "</span>";
});

var arc = d3.svg.arc()
.innerRadius(innerRadius)
.outerRadius(function (d) {
  return ((radius - innerRadius) * (d.data.score / highestScore) + innerRadius);
});

var outlineArc = d3.svg.arc()
.innerRadius(innerRadius)
.outerRadius(radius);

var svg = d3.select("body").append("svg")
.attr('class', 'd4-graph')
.attr("width", width)
.attr("height", height)
.append("g")
.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

svg.call(tip);

d3.csv('data.csv', function (error, data) {

  // Predefined colors to use
  var colors = ["#9E0041", "#C32F4B", "#E1514B", "#F47245", "#FB9F59", "#FEC574", "#FAE38C", "#EAF195", "#C7E89E", "#9CD6A4", "#6CC4A4", "#4D9DB4", "#4776B4", "#5E4EA1"];
  var colorCount = 0;

  // For each user update highest score if necessary
  data.forEach(function (d) {
    if (highestScore < parseInt(d.score)) {
      highestScore = parseInt(d.score);
    }
  });

  data.forEach(function (d) {
    d.id = d.id;
    d.order = 1;
    // Select from predefined colors
    d.color = colors[colorCount];
    if (colorCount == 13) {
      colorCount = 0;
    }
    else {
      colorCount++;
    }
    d.weight = .1;
    d.score = +d.score;
    d.width = +d.weight;
    d.level = d.currentlevel;
  });

  var path = svg.selectAll(".solidArc")
  .data(pie(data))
  .enter().append("path")
  .attr("fill", function (d) { return d.data.color; })
  .attr("class", "solidArc")
  .attr("stroke", "white")
  .attr("d", arc)
  .on('mouseover', tip.show)
  // Invoke showSelectedSudent to show more info
  .on('click', function (d) { showSelectedSudent(d.data);})
  .on('mouseout', tip.hide);

  var outerPath = svg.selectAll(".outlineArc")
  .data(pie(data))
  .enter().append("path")
  .attr("fill", "none")
  .attr("stroke", "gray")
  .attr("class", "outlineArc")

  var score =
  data.reduce(function (a, b) {
    return a + (b.score * b.weight);
  }, 0) /
  data.reduce(function (a, b) {
    return a + b.weight;
  }, 0);

  svg.append("svg:text")
  .attr("class", "aster-score")
  .attr("dy", ".35em")
  .attr("text-anchor", "middle")
  .text(Math.round(score));

  svg.append("svg:text")
  .attr("class", "score-info")
  .attr("y", "30")
  .attr("dy", ".35em")
  .attr("text-anchor", "middle")
  .text("Mean Score");
});
