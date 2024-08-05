resource "aws_s3_bucket" "public_bucket" {
  bucket = "bucket-abc"
  force_destroy = true

  tags = {
    Name        = "My Public bucket"
    Environment = "Dev"
  }
}

resource "aws_s3_bucket_policy" "example_policy" {
  bucket = aws_s3_bucket.public_bucket.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Sid       = "AllowAllActions",
        Effect    = "Allow",
        Principal = "*",
        Action    = "s3:*",
        Resource  = "${aws_s3_bucket.public_bucket.arn}/*",
        Condition = {
          IpAddress = {
            "aws:SourceIp": "aa.bb.cc.dd/32"  # Replace with your allowed IP address or range
          }
        }
      }
    ]
  })
}

