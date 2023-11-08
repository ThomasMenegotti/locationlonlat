<h1 align="center">Mobile Application Development: Location Services</h1>

<p align="center">
  <strong>Assignment 2</strong>
</p>

<p align="center">Thomas Menegotti [100750648]</p>

<p align="center">Date: October 8rd, 2023</p>

<h2 align="center">Introduction</h2>

<p>
  The objective of this assignment is to practice Android application development with databases and using Geocoding for location services.
</p>

<h2 align="center">Requirements</h2>

<ol>
  <li>Use an input file of 50 latitude and longitude pairs, then read these pairs and use Geocoding to associate an address to these pairs.</li>
  <li>Create a database and a location table with four columns (id, address, latitude, longitude).</li>
  <li>Allow users to search for an address and if it is within the database, display corresponding longitude and latitude coordinates.</li>
  <li>Allow users to add given they provide an address, find the latitude and longitude pairs associated with that address, and store it in the database.</li>
  <li>Allow users to delete given they already have the address searched up with latitude and longitude fields populated.</li>
  <li>Allow users to edit the address associated with the current latitude and longitude coordinates to the current text in the address field.</li>
</ol>

<h2 align="center">How to Use</h2>

<p>
  To make the most of this Android application, follow these steps:
</p>

<ol>
  <li><strong>Display Latitude and Longitude Coordinates</strong><br>
    To display the latitude and longitude coordinates, input the full address line as it is stored in the database and press the "Show Latitude and Longitude" button.
    <br>Example: "1600 Amphitheatre Parkway, Mountain View, CA"
  </li>

  <li><strong>Edit Address Data</strong><br>
    To edit the address data, follow these steps:
    <ol>
      <li>Search for an address by providing the full address in the "Enter Full Address Line" field and press the "Show Latitude and Longitude" button.</li>
      <li>Once the fields are populated with the latitude and longitude, input the new address you want to save in the database.</li>
      <li>Press the "Edit Address Data" button to update the address in the database.
    </ol>
    Example: Change "1600 Amphitheatre Parkway, Mountain View, CA" to "New Address".
  </li>

  <li><strong>Get and Add Data</strong><br>
    To add a new entry to the database, follow these steps:
    <ol>
      <li>Input the new address you wish to add to the database in the "Enter Full Address Line" field.</li>
      <li>Press the "Get and Add Data" button to use forward geocoding to find the latitude and longitude coordinates associated with the address and add it to the database.
    </ol>
    Example: "1 Infinite Loop, Cupertino, CA"
  </li>

  <li><strong>Delete Data</strong><br>
    To delete an entry from the database, follow these steps:
    <ol>
      <li>Populate the latitude and longitude fields by pressing the "Show Latitude and Longitude" button with the desired address.</li>
      <li>Press the "Delete Data" button to remove all entries in the database that match the latitude and longitude pairs.
    </ol>
    Example: Deleting the entry associated with "1 Infinite Loop, Cupertino, CA".
  </li>
</ol>

<h2 align="center">Proof</h2>

<p>
  For detailed proof of requirements 1 to 6, view the report.
</p>

<h2 align="center">Best Practices for GeoCoding</h2>

<p>
  The application follows best practices for GeoCoding, ensuring accurate location data retrieval.
</p>
