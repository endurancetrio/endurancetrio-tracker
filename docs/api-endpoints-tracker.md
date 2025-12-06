# API Reference - Tracker Domain

Complete endpoint documentation for the Tracker resource within the **EnduranceTrio Tracker**
REST API. Includes request/response schemas, examples, error handling, and authentication
requirements. For an overview of the project, see the [main README.md](../README.md).

## Table of Contents

1. [Submit a device location](#1-submit-a-device-location)
2. [Get last known location for all existing devices](#2-get-last-known-location-for-all-existing-devices)
3. [Get historical locations for a device](#3-get-historical-locations-for-a-device)

## 1. Submit a device location

```shell
POST /tracker/v1/devices
Content-Type: application/json
Authorization: Bearer api-key-here
ET-Owner: account-name-here

{
  "device": "SDABC",
  "time": "2026-09-19T06:00:00Z",
  "lat": 39.510058,
  "lon": -9.136079,
  "active": true
}
```

**Response**: `201 Created`

```json
{
  "code": 201,
  "status": "Created",
  "details": "Request handled successfully",
  "data": {
    "device": "SDABC",
    "time": "2026-09-19T06:00:00Z",
    "lat": 39.510058,
    "lon": -9.136079,
    "active": true
  }
}
```

### `cURL` request (assuming the application is running on localhost:8081):

```shell
curl -X POST 'http://localhost:8081/api/tracker/v1/devices' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <api-key-here>' \
  -H 'ET-Owner: <account-name-here>' \
  -d '{
    "device": "SDABC",
    "time": "2026-09-19T06:00:00Z",
    "lat": 39.510058,
    "lon": -9.136079,
    "active": true
  }'
```

## 2. Get last known location for all existing devices

```shell
GET /tracker/v1/devices
Content-Type: application/json
Authorization: Bearer api-key-here
ET-Owner: account-name-here
```

**Response**: `200 OK`

```json
{
  "code": 200,
  "status": "OK",
  "details": "Request handled successfully",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.510058,
      "lon": -9.136079,
      "active": true
    },
    {
      "device": "SDDEF",
      "time": "2026-09-19T06:00:06Z",
      "lat":  39.509001,
      "lon": -9.139602,
      "active": true
    },
    {
      "device": "SDFGH",
      "time": "2026-09-19T06:00:12Z",
      "lat": 39.509773,
      "lon": -9.140004,
      "active": true
    },
    {
      "device": "SDJKL",
      "time": "2026-09-19T06:00:24Z",
      "lat": 39.511075,
      "lon":  -9.136516,
      "active": true
    }
  ]
}
```

### `cURL` request (assuming the application is running on localhost:8081):

```shell
curl -X GET 'http://localhost:8081/api/tracker/v1/devices' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <api-key-here>' \
  -H 'ET-Owner: <account-name-here>'
```

## 3. Get historical locations for a device

**To be implemented**

```shell
GET /tracker/v1/devices/{deviceId}/locations?page=0&size=20
Content-Type: application/json
Authorization: Bearer api-key-here
ET-Owner: account-name-here
```

**Response**: `200 OK`

```json
{
  "code": 200,
  "status": "OK",
  "details": "Request handled successfully",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.510058,
      "lon": -9.136079,
      "active": true
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:06:00Z",
      "lat": 39.510071,
      "lon": -9.136071,
      "active": true
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:12:00Z",
      "lat": 39.510082,
      "lon": -9.136062,
      "active": true
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:24:00Z",
      "lat": 39.510093,
      "lon": -9.136053,
      "active": true
    }
  ],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 4,
    "totalPages": 1
  }
}
```
