Stats Page Request to Server from Client:
-----------------------------------------

http://our-server-address/?showStat=teacherUsername

User Deletion Request to Server from Stats Page:
--------
http://our-server-address/delete/?teacher=username1&student=username2

CSV:
--------
Required column headers for graph: **id**, **score**, **currentlevel**.
You can have more data/columns, but the above are required for minimum information on graph. Any extra columns will be automatically pulled from CSV. Table will include all of csv data. Example CSV Format:

"City","State","Country"

"Salt Lake City","UT","USA"

"Los Angeles", "CA", "USA"

Search, Highlight & Anchor:
--------
 - Highlights user data in table when searched for, clicked on graph, or clicked on "MORE INFO".
 - Anchors to user row when searched for username, clicked on graph or clicked on "MORE INFO".
 - Search allows to find users based on score, level, username, name, or any other field available.
 - Search is capable of highlighting multiple rows if search term is applicable to multiple users.
 - Searching a username will anchor the page to that user's row.
