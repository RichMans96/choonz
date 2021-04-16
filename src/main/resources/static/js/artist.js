function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
    vars[key] = value;
  });
  return vars;
}

const artistId = getUrlVars()['id'];

async function fetchArtist() {
  const response = await fetch(`http://localhost:8082/artists/read/id/${artistId}`);

  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }

  const artist = await response.json();
  return artist;
}

async function fetchAlbums() {
  const response = await fetch(`http://localhost:8082/albums/read`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const albums = await response.json();
  return albums;
}

function sort() {
  const albumCards = document.querySelectorAll('.col-lg-4.col-sm-6.mb-4');
  const sortButton = document.querySelector('#sort');
  for (let i = 0; i < albumCards.length; i++) {
    if (albumCards[i].getAttribute('data-featuredartists') < 1) {
      if (albumCards[i].style.display == 'block') {
        albumCards[i].style.display = 'none';
        sortButton.innerHTML = 'Click to view all';
      } else {
        albumCards[i].style.display = 'block';
        sortButton.innerHTML = 'Click for albums with featured Artists';
      }
    }
  }
}

fetchAlbums()
  .then((albums) => {
    fetchArtist()
      .then((artist) => {
        const artistName = document.querySelector('.my-4');
        const albumRow = document.querySelector('.row');

        artistName.innerHTML = artist.name;
        document.title = `Choonz Tracks | ${artist.name}`;

        for (const album of artist.albums) {
          const cardContainer = document.createElement('div');
          const cardWrapper = document.createElement('div');
          const cardBody = document.createElement('div');
          const albumTitle = document.createElement('h4');
          const albumLink = document.createElement('a');

          for (let i = 0; i < albums.length; i++) {
            if (albums[i].id === album) {
              cardContainer.id = albums[i].id;
              cardContainer.setAttribute('style', 'display: block');
              if (albums[i].featuredArtists == 0) {
                cardContainer.setAttribute('data-featuredArtists', '0');
              }
              cardContainer.setAttribute('data-featuredArtists', albums[i].featuredArtists);
              cardContainer.className = 'col-lg-4 col-sm-6 mb-4';
              cardWrapper.className = 'card h-100';
              cardBody.className = 'card-body';
              albumTitle.className = 'card-title';
              albumTitle.innerHTML = albums[i].name;
              albumLink.innerHTML = 'click here to view';
              albumLink.href = '/album?id=' + album;

              cardWrapper.appendChild(albumTitle);
              cardWrapper.appendChild(albumLink);
              cardWrapper.appendChild(cardBody);
              cardContainer.appendChild(cardWrapper);
              albumRow.appendChild(cardContainer);
            }
          }
        }
      })
      .catch((error) => error.message);
  })
  .catch((error) => error.message);
